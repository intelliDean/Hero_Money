package com.loan.hero.loan_officer.services;

import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.agreement.service.LoanAgreementService;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.auth.security.utility.JwtService;
import com.loan.hero.auth.user.data.models.Address;
import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.auth.user.service.HeroTokenService;
import com.loan.hero.auth.user.service.UserService;
import com.loan.hero.cloud_service.CloudService;
import com.loan.hero.exceptions.HeroException;
import com.loan.hero.exceptions.UserNotAuthorizedException;
import com.loan.hero.exceptions.UserNotFoundException;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan.data.models.LoanStatus;
import com.loan.hero.loan.service.LoanService;
import com.loan.hero.loan_officer.data.dto.request.AgreementRequest;
import com.loan.hero.loan_officer.data.dto.request.OfficerRequest;
import com.loan.hero.loan_officer.data.dto.request.UpdateLoanRequest;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import com.loan.hero.loan_officer.data.repositories.LoanOfficerRepository;
import com.loan.hero.init_token.InitToken;
import com.loan.hero.init_token.service.InitTokenService;
import com.loan.hero.notification.mail.MailService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.loan.hero.auth.user.data.models.Role.LOAN_OFFICER;
import static com.loan.hero.hero_utility.HeroUtilities.MAX_NUMBER_PER_PAGE;

@Service
@AllArgsConstructor
public class LoanOfficerServiceImpl implements LoanOfficerService {
    private final LoanOfficerRepository loanOfficerRepository;
    private final LoanAgreementService loanAgreementService;
    private final InitTokenService initTokenService;
    private final HeroTokenService heroTokenService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final CloudService cloudService;
    private final LoanService loanService;
    private final MailService mailService;
    private final UserService userService;
    private final JwtService jwtService;

    @Override
    public LoanOfficer cuurentLoanOfficer() {
        return loanOfficerRepository.findByUser(userService.getCurrentUser())
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AuthenticationToken completeOfficerProfile(OfficerRequest request) {
        final LoanOfficer loanOfficer = findByUserEmail(request.getEmail());
        final InitToken initToken = initTokenService.findByTokenAndEmail(
                request.getToken(),
                request.getEmail()
        ).orElseThrow(UserNotAuthorizedException::new);

        if (initTokenService.isValid(initToken)
                && loanOfficer.getEmployeeId().equals(request.getEmployeeId())) {
            final User user = loanOfficer.getUser();
            final Address address = createAddress(request);

            user.setPhoneNumber(request.getPhoneNumber());
            user.setAddress(address);
            user.setEnabled(true);
            user.setRoles(Collections.singleton(LOAN_OFFICER));
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setUserImage(uploadImage(request.getUserImage()));
            loanOfficer.setUser(user);

            final String accessToken = jwtService.generateAccessToken(
                    getUserAuthority(user),
                    user.getEmail()
            );
            final String refreshToken = jwtService.generateRefreshToken(user.getEmail());

            final HeroToken heroToken = HeroToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(user)
                    .revoked(false)
                    .build();
            heroTokenService.saveToken(heroToken);

            return AuthenticationToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new UserNotAuthorizedException();
    }

    private Address createAddress(OfficerRequest request) {
        return Address.builder()
                .houseNumber(request.getHouseNumber())
                .streetName(request.getStreetName())
                .city(request.getCity())
                .state(request.getState())
                .zipCode(request.getZipCode())
                .build();
    }


    private static Map<String, Object> getUserAuthority(User savedUser) {
        return savedUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(
                        Collectors.toMap(
                                authority -> "claim",
                                Function.identity()
                        )
                );
    }

    private String uploadImage(MultipartFile file) {
        try {
            return cloudService.uploadFile(file);
        } catch (RuntimeException e) {
            throw new HeroException("Image upload failed");
        }
    }

    @Override
    public LoanOfficer findByUserEmail(String email) {
        return loanOfficerRepository.findByUserEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public LoanStatus updateLoanStatus(UpdateLoanRequest request) {
        final Loan loan = updateLoan(request);
        return loan.getLoanStatus();
    }

    @Override
    public LoanStatus approveLoanApplication(Long loanId) {
        final UpdateLoanRequest request = UpdateLoanRequest.builder()
                .loanId(loanId)
                .loanStatus(LoanStatus.APPROVED)
                .build();
        return updateLoan(request).getLoanStatus();
    }

    @Override
    public LoanStatus rejectLoanApplication(Long loanId) {
        final UpdateLoanRequest request = UpdateLoanRequest.builder()
                .loanId(loanId)
                .loanStatus(LoanStatus.REJECTED)
                .build();
        return updateLoan(request).getLoanStatus();
    }

    private Loan updateLoan(UpdateLoanRequest request) {
        final Loan loan = loanService.findById(request.getLoanId());
        loan.setLoanStatus(request.getLoanStatus());
        return loanService.saveLoan(loan);
    }
    @Override
    public Page<Loan> allFreshLoans(int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        final Pageable pageable = PageRequest.of(page, MAX_NUMBER_PER_PAGE);
        final List<Loan> freshLoans = loanService.allFreshApplication();
        return new PageImpl<>(freshLoans, pageable, freshLoans.size());
    }

    @Override
    public Page<LoanAgreement> allLoanAgreementByOfficerId(int pageNumber) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        final Pageable pageable = PageRequest.of(page, MAX_NUMBER_PER_PAGE);
        final List<LoanAgreement> allAgreements = loanAgreementService.allAgreementsByLoanOfficer(
                cuurentLoanOfficer().getId()
        );
        return new PageImpl<>(allAgreements, pageable, allAgreements.size());
    }

    @Override
    public LoanAgreement generateAgreement(AgreementRequest request) {
        final Loan loan = loanService.approvedApplication(request.getLoanId());
        loan.setInterestRate(request.getInterestRate());
        loan.setStartDate(LocalDateTime.now());
        loan.setEndDate(loan.getStartDate().plusYears(loan.getRepaymentTerm()));
        loan.setDisbursementDate(LocalDateTime.now());

       final BigDecimal interestRate = loan.getInterestRate()
                .divide(BigDecimal.valueOf(100), RoundingMode.HALF_DOWN);

        switch (loan.getPaymentFrequency()) {
            case WEEKLY -> weeklyPlan(loan, interestRate);
            case MONTHLY -> monthlyPlan(loan, interestRate);
            case QUARTERLY -> quarterlyPlan(loan, interestRate);
        }
        return loanAgreementService.saveAgreement(loan, cuurentLoanOfficer());
    }

    private void quarterlyPlan(Loan loan, BigDecimal interestRate) {
        int quarterPerYear = 4;
        final BigDecimal quarterlyInterestRate = interestRate
                .divide(BigDecimal.valueOf(quarterPerYear), RoundingMode.UP);
        int totalNumberOfQuarters = loan.getRepaymentTerm() * quarterPerYear;
        loan.setRepaymentAmount(
                loanPayment(
                        loan.getLoanAmount(),
                        quarterlyInterestRate,
                        totalNumberOfQuarters
                )
        );
    }

    private void monthlyPlan(Loan loan, BigDecimal interestRate) {
        int monthsPerYear = 12;
        final BigDecimal monthlyInterestRate = interestRate
                .divide(BigDecimal.valueOf(monthsPerYear), RoundingMode.UP);
        int totalNumberOfMonths = loan.getRepaymentTerm() * monthsPerYear;
        loan.setRepaymentAmount(
                loanPayment(
                        loan.getLoanAmount(),
                        monthlyInterestRate,
                        totalNumberOfMonths
                )
        );
    }

    private void weeklyPlan(Loan loan, BigDecimal interestRate) {
        int weeksPerYear = 52;
        final BigDecimal weeklyInterestRate = interestRate
                .divide(BigDecimal.valueOf(weeksPerYear), RoundingMode.UP);
        int totalNumberOfWeeks = loan.getRepaymentTerm() * weeksPerYear;
        loan.setRepaymentAmount(
                loanPayment(
                        loan.getLoanAmount(),
                        weeklyInterestRate,
                        totalNumberOfWeeks
                )
        );
    }

    private BigDecimal loanPayment(
            BigDecimal loanAmount,
            BigDecimal interestRate,
            int numberOfPayments
    ) {
        final BigDecimal present = BigDecimal.valueOf(
                Math.pow(
                        1 + interestRate.doubleValue(),
                        numberOfPayments
                )
        );
        final BigDecimal presentValueFactor = (present.subtract(
                BigDecimal.valueOf(1))
        ).divide(interestRate.multiply(present), RoundingMode.HALF_DOWN);
        return loanAmount.divide(presentValueFactor, 2, RoundingMode.UP);
    }
}