package com.api.xpress.customer.services;

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
import com.api.xpress.customer.data.dto.request.Decision;
import com.api.xpress.customer.data.dto.request.InitRequest;
import com.api.xpress.customer.data.dto.request.SignUpRequest;
import com.api.xpress.customer.data.dto.request.UpdateCustomerRequest;
import com.api.xpress.customer.data.models.enums.AgreementDecision;
import com.api.xpress.customer.data.dto.response.InitResponse;
import com.api.xpress.customer.data.models.Customer;
import com.api.xpress.customer.data.repositories.CustomerRepository;
import com.api.xpress.exceptions.XpressException;
import com.api.xpress.exceptions.UserNotFoundException;
import com.api.xpress.exceptions.UserNotAuthorizedException;
import com.api.xpress.xpress_utils.XpressUtils;
import com.api.xpress.loan.data.dto.request.LoanRequest;
import com.api.xpress.loan.data.dto.response.LoanDTO;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan.data.models.LoanDocuments;
import com.api.xpress.loan.data.models.LoanStatus;
import com.api.xpress.loan.service.LoanService;
import com.api.xpress.notification.InitToken;
import com.api.xpress.notification.dto.EmailRequest;
import com.api.xpress.notification.dto.MailInfo;
import com.api.xpress.notification.interfaces.InitTokenService;
import com.api.xpress.notification.interfaces.MailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.api.xpress.auth.user.data.models.Role.CUSTOMER;
import static com.api.xpress.customer.data.models.enums.AgreementDecision.ACCEPT;
import static com.api.xpress.customer.data.models.enums.AgreementDecision.REJECT;
import static com.api.xpress.loan.data.models.LoanStatus.ACTIVE;
import static com.api.xpress.loan.data.models.LoanStatus.CLOSED;


@Service
@Transactional
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final LoanAgreementService loanAgreementService;
    private final CustomerRepository customerRepository;
    private final XpressTokenService xpressTokenService;
    private final InitTokenService initTokenService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final MultimediaService multimediaService;
    private final LoanService loanService;
    private final MailService mailService;
    private final JwtService jwtService;

    @Override
    public InitResponse initAccess(InitRequest initRequest) {
        if (customerRepository.existsByUserEmail(initRequest.email())) {
            return InitResponse.LOGIN;
        } else {
            try {
                sendSignUpMail(initRequest.email());
            } catch (Exception e) {
                throw new XpressException("Registration failed: " + e.getMessage());
            }
            return InitResponse.SIGNUP;
        }
    }

    private void sendSignUpMail(String email) {
        final String token = XpressUtils.generateToken(7);
        initTokenService.saveToken(
                InitToken.builder()
                        .token(token)
                        .email(email)
                        .expireAt(LocalDateTime.now().plusHours(3))
                        .build()
        );

        int atIndex = email.indexOf("@");
        final String username = email.substring(0, atIndex);

        final Context context = new Context();
        context.setVariables(
                Map.of(
                        "username", username,
                        "token", token
                )
        );
        final String content = templateEngine.process("customer_mail", context);

        final EmailRequest emailRequest = EmailRequest.builder()
                .to(List.of(new MailInfo(username, email)))
                .subject("Welcome to Xpress")
                .htmlContent(content)
                .build();
        mailService.sendMail(emailRequest);
    }

    @Override
    public AuthenticationToken register(SignUpRequest signUpRequest) {
        final InitToken initToken = initTokenService.findByTokenAndEmail(
                signUpRequest.token(),
                signUpRequest.email()
        ).orElseThrow(XpressException::new);
        int age = getAge(signUpRequest.dateOfBirth());
        if (!initTokenService.isValid(initToken) || age < 18) {
            throw new XpressException("Invalid credentials");
        }

        final User user = User.builder()
                .firstName(signUpRequest.firstName().trim())
                .lastName(signUpRequest.lastName().trim())
                .email(signUpRequest.email().trim())
                .password(passwordEncoder.encode(signUpRequest.password().trim()))
                .enabled(true)
                .roles(Set.of(CUSTOMER))
                .build();

        final Customer customer = Customer.builder()
                .user(user)
                .age(age)
                .complete(false)
                .build();
        customerRepository.save(customer);

        initToken.setRevoked(true);
        initTokenService.saveToken(initToken);

        return getAuthenticationToken(user);
    }

    private int getAge(String dateOfBirth) {
        final LocalDate birthDate = LocalDate.parse(
                dateOfBirth.trim(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        final Period period = Period.between(
                birthDate,
                LocalDate.now()
        );
        return period.getYears();
    }

    @Override
    public Customer getCurrentCustomer(AuthenticatedUser currentUser) {
        return customerRepository.findCustomerByUserEmail(currentUser.getUsername())
                .orElseThrow(UserNotFoundException::new);
    }


    @Override
    public String uploadCustomerImage(MultipartFile image, AuthenticatedUser currentUser) {
        final Customer customer = getCurrentCustomer(currentUser);
        final String imageUrl = uploadImage(image);
        customer.getUser().setUserImage(imageUrl);
        customerRepository.save(customer);
        return "Image uploaded successfully";
    }

    @Override
    public LoanDTO apply(LoanRequest request, AuthenticatedUser currentUser) {
        final Customer customer = getCurrentCustomer(currentUser);
        if (!customer.isComplete()) {
            throw new XpressException("Complete profile update");
        }
        final LoanDocuments loanDocuments = LoanDocuments.builder()
                .paySlip(uploadImage(request.paySlip()))
                .bankStatement(uploadImage(request.accountStatement()))
                .build();

        final Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(request.loanAmount())
                .loanPurpose(request.loanPurpose())
                .loanDocuments(loanDocuments)
                .paymentFrequency(request.paymentFrequency())
                .repaymentTerm(request.repaymentTerm())
                .applicationDate(LocalDateTime.now())
                .loanStatus(LoanStatus.PENDING)
                .build();
        final Loan savedLoan = loanService.saveLoan(loan);

        sendMail(customer, savedLoan);

        return LoanDTO.builder()
                .message("Application successful! Please check your email")
                .applicationDate(savedLoan.getApplicationDate())
                .loanStatus(savedLoan.getLoanStatus())
                .build();
    }

    @Override
    public LoanStatus viewLoanStatus(Long loanId, AuthenticatedUser currentUser) {
        final Loan loan = loanService.findById(loanId);
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CUSTOMER"))) {
            Customer customer = getCurrentCustomer(currentUser);
            if (!loan.getCustomer().getId().equals(customer.getId())) {
                throw new UserNotAuthorizedException("You are not authorized to view this loan status");
            }
        }
        return loan.getLoanStatus();
    }

    @Override
    public Map<String, String> allLoansStatus(AuthenticatedUser currentUser) {
        final List<Loan> allLoansByCustomer = loanService.allLoansByCustomerId(
                getCurrentCustomer(currentUser).getId()
        );

        final Map<String, String> allLoansStatus = new HashMap<>();
        allLoansByCustomer.forEach(
                loan -> allLoansStatus.put(
                        loan.getId().toString(),
                        loan.getLoanStatus().name()
                )
        );
        return allLoansStatus;
    }

    @Override
    public LoanAgreement viewAgreement(Long agreementId, AuthenticatedUser currentUser) {
        final LoanAgreement loanAgreement = loanAgreementService.findById(agreementId);
        if (currentUser.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("CUSTOMER"))) {
            Customer customer = getCurrentCustomer(currentUser);
            if (!loanAgreement.getLoan().getCustomer().getId().equals(customer.getId())) {
                throw new UserNotAuthorizedException("You are not authorized to view this agreement");
            }
        }
        return loanAgreement;
    }

    private void sendMail(Customer customer, Loan savedLoan) {
        final String name = customer.getUser().getFirstName();
        final Context context = new Context();
        context.setVariables(
                Map.of(
                        "name", name,
                        "amount", savedLoan.getLoanAmount().toString(),
                        "status", savedLoan.getLoanStatus().name()
                )
        );
        final String content = templateEngine.process("loan_application_mail", context);
        final EmailRequest emailRequest = EmailRequest.builder()
                .to(List.of(new MailInfo(name, customer.getUser().getEmail())))
                .subject("Application Update")
                .htmlContent(content)
                .build();

        mailService.sendMail(emailRequest);
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
    public Customer updateCustomerProfile(UpdateCustomerRequest request, AuthenticatedUser currentUser) {
        final Customer customer = getCurrentCustomer(currentUser);
        final User user = updateUser(request, customer);

        customer.setUser(user);
        customer.setGender(request.gender());
        customer.setSalary(request.salary());
        customer.setCompanyName(request.companyName());
        customer.setJobStatus(request.jobStatus());
        customer.setMaritalStatus(request.maritalStatus());
        customer.setFormOfIdentity(uploadImage(request.formOfIdentity()));
        customer.setComplete(true);
        return customerRepository.save(customer);
    }

    @Override
    public AgreementDecision agreementDecision(Decision decision, AuthenticatedUser currentUser) {
        final LoanAgreement loanAgreement = loanAgreementService.findById(decision.loanAgreementId());
        Customer customer = getCurrentCustomer(currentUser);
        if (!loanAgreement.getLoan().getCustomer().getId().equals(customer.getId())) {
            throw new UserNotAuthorizedException("You are not authorized to make a decision on this agreement");
        }
        switch (decision.agreementDecision()) {
            case ACCEPT -> {
                loanAgreement.setAgreed(true);
                loanAgreement.getLoan().setLoanStatus(ACTIVE);
                loanAgreementService.save(loanAgreement);
                return ACCEPT;
            }
            case REJECT -> {
                loanAgreement.setAgreed(false);
                loanAgreement.getLoan().setLoanStatus(CLOSED);
                loanAgreementService.save(loanAgreement);
                return REJECT;
            }
        }
        throw new XpressException();
    }

    private User updateUser(UpdateCustomerRequest request, Customer customer) {
        final User user = customer.getUser();
        user.setUserImage(uploadImage(request.userImage()));
        user.setPhoneNumber(request.phoneNumber());
        user.setAddress(
                Address.builder()
                        .houseNumber(request.houseNumber())
                        .streetName(request.streetName())
                        .city(request.city())
                        .state(request.state())
                        .zipCode(request.zipCode())
                        .build()
        );
        return user;
    }

    private AuthenticationToken getAuthenticationToken(User user) {
        final String email = user.getEmail();
        final String accessToken = jwtService.generateAccessToken(
                getUserAuthority(user),
                email
        );
        final String refreshToken = jwtService.generateRefreshToken(email);

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
}
