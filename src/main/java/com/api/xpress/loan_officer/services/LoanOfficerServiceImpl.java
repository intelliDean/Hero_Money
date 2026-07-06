package com.api.xpress.loan_officer.services;

import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.agreement.service.LoanAgreementService;
import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.auth.security.utility.JwtService;
import com.api.xpress.auth.user.data.models.Address;
import com.api.xpress.auth.user.data.models.XpressToken;
import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.auth.user.service.XpressTokenService;
import com.api.xpress.auth.user.service.UserService;
import com.api.xpress.multimedia.MultimediaService;
import com.api.xpress.exceptions.XpressException;
import com.api.xpress.exceptions.UserNotAuthorizedException;
import com.api.xpress.exceptions.UserNotFoundException;
import com.api.xpress.xpress_utils.XpressUtils;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan.data.models.LoanStatus;
import com.api.xpress.loan.service.LoanService;
import com.api.xpress.loan_officer.data.dto.request.AgreementRequest;
import com.api.xpress.loan_officer.data.dto.request.InviteRequest;
import com.api.xpress.loan_officer.data.dto.request.OfficerRequest;
import com.api.xpress.loan_officer.data.dto.request.UpdateLoanRequest;
import com.api.xpress.loan_officer.data.models.LoanOfficer;
import com.api.xpress.loan_officer.data.repositories.LoanOfficeRepository;
import com.api.xpress.notification.InitToken;
import com.api.xpress.notification.dto.EmailRequest;
import com.api.xpress.notification.dto.MailInfo;
import com.api.xpress.notification.interfaces.InitTokenService;
import com.api.xpress.notification.interfaces.MailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.api.xpress.auth.user.data.models.Role.LOAN_OFFICER;
import static com.api.xpress.xpress_utils.XpressUtils.MAX_NUMBER_PER_PAGE;

@Service
@Transactional
@RequiredArgsConstructor
public class LoanOfficerServiceImpl implements LoanOfficerService {
    private final LoanOfficeRepository loanOfficeRepository;
    private final LoanAgreementService loanAgreementService;
    private final InitTokenService initTokenService;
    private final XpressTokenService xpressTokenService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final MultimediaService multimediaService;
    private final LoanService loanService;
    private final MailService mailService;
    private final UserService userService;
    private final JwtService jwtService;


    @Override
    public String inviteAdmin(InviteRequest request) {
        final User user = User.builder()
                .email(request.email())
                .firstName(request.firstName().trim())
                .lastName(request.lastName().trim())
                .build();
        final LoanOfficer loanOfficer = LoanOfficer.builder()
                .user(user)
                .build();
        final LoanOfficer savedLoanOfficer = loanOfficeRepository.save(loanOfficer);
        savedLoanOfficer.setEmployeeId(employeeId(savedLoanOfficer));
        loanOfficeRepository.save(savedLoanOfficer);

        sendLoanOfficerInvite(request, savedLoanOfficer);
        return "Loan officer invite sent successfully";
    }

    @Override
    public LoanOfficer currentLoanOfficer(AuthenticatedUser currentUser) {
        return loanOfficeRepository.findLoanOfficerByUserEmail(currentUser.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public AuthenticationToken completeOfficerProfile(OfficerRequest request) {
        final LoanOfficer loanOfficer = findByUserEmail(request.email());
        final InitToken initToken = initTokenService.findByTokenAndEmail(
                request.token(),
                request.email()
        ).orElseThrow(UserNotAuthorizedException::new);

        if (initTokenService.isValid(initToken)
                && loanOfficer.getEmployeeId().equals(request.employeeId())) {
            final User user = loanOfficer.getUser();
            final Address address = createAddress(request);

            user.setPhoneNumber(request.phoneNumber());
            user.setAddress(address);
            user.setEnabled(true);
            user.setRoles(Collections.singleton(LOAN_OFFICER));
            user.setPassword(passwordEncoder.encode(request.password()));
            user.setUserImage(uploadImage(request.userImage()));
            loanOfficer.setUser(user);

            final String accessToken = jwtService.generateAccessToken(
                    getUserAuthority(user),
                    user.getEmail()
            );
            final String refreshToken = jwtService.generateRefreshToken(user.getEmail());

            final XpressToken xpressToken = XpressToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .user(user)
                    .revoked(false)
                    .build();
            xpressTokenService.saveToken(xpressToken);

            return AuthenticationToken.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        throw new UserNotAuthorizedException();
    }

    private Address createAddress(OfficerRequest request) {
        return Address.builder()
                .houseNumber(request.houseNumber())
                .streetName(request.streetName())
                .city(request.city())
                .state(request.state())
                .zipCode(request.zipCode())
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
        XpressUtils.validateFile(file);
        try {
            return multimediaService.uploadFile(file);
        } catch (RuntimeException e) {
            throw new XpressException("Image upload failed");
        }
    }

    @Override
    public LoanOfficer findByUserEmail(String email) {
        return loanOfficeRepository.findLoanOfficerByUserEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    private void sendLoanOfficerInvite(InviteRequest request, LoanOfficer savedLoanOfficer) {
        final String token = XpressUtils.generateToken(10);

        final InitToken initToken = InitToken.builder()
                .token(token)
                .email(request.email())
                .revoked(false)
                .build();
        initTokenService.saveToken(initToken);

        final String fullName = "%s %s".formatted(request.firstName(), request.lastName());
        final Context context = new Context();
        context.setVariable("fullName", fullName);
        context.setVariable("id", savedLoanOfficer.getEmployeeId());
        context.setVariable("token", token);

        final String content = templateEngine.process("admin_invite", context);

        final EmailRequest emailRequest = EmailRequest.builder()
                .to(Collections.singletonList(new MailInfo(fullName, request.email())))
                .subject("Welcome Aboard")
                .htmlContent(content)
                .build();

        mailService.sendMail(emailRequest);
    }

    private String employeeId(LoanOfficer loanOfficer) {
        final String firstLetters = String.format("%s%s",
                loanOfficer.getUser().getFirstName().charAt(0),
                loanOfficer.getUser().getLastName().charAt(0)
        );
        final String toUppercase = firstLetters.toUpperCase();
        final String loanOfficerId = String.valueOf(loanOfficer.getId());
        final String userId = String.valueOf(loanOfficer.getUser().getId());
        return String.format("%s-0%s-0%s", toUppercase, userId, loanOfficerId);
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
        final Loan loan = loanService.findById(request.loanId());
        loan.setLoanStatus(request.loanStatus());
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
    public Page<LoanAgreement> allLoanAgreementByOfficerId(int pageNumber, AuthenticatedUser currentUser) {
        int page = pageNumber < 1 ? 0 : pageNumber - 1;
        final Pageable pageable = PageRequest.of(page, MAX_NUMBER_PER_PAGE);
        final List<LoanAgreement> allAgreements = loanAgreementService.allAgreementsByLoanOfficer(
                currentLoanOfficer(currentUser).getId()
        );
        return new PageImpl<>(allAgreements, pageable, allAgreements.size());
    }

    @Override
    public LoanAgreement generateAgreement(AgreementRequest request, AuthenticatedUser currentUser) {
        final Loan loan = loanService.approvedApplication(request.loanId());
        loan.setInterestRate(request.interestRate());
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
        return loanAgreementService.saveAgreement(loan, currentLoanOfficer(currentUser));
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