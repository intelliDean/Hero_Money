package com.loan.hero.customer.services;

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
import com.loan.hero.customer.data.dto.request.Decision;
import com.loan.hero.customer.data.dto.request.InitRequest;
import com.loan.hero.customer.data.dto.request.SignUpRequest;
import com.loan.hero.customer.data.dto.request.UpdateCustomerRequest;
import com.loan.hero.customer.data.models.enums.AgreementDecision;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.customer.data.repositories.CustomerRepository;
import com.loan.hero.exceptions.HeroException;
import com.loan.hero.exceptions.UserNotFoundException;
import com.loan.hero.hero_utility.HeroUtilities;
import com.loan.hero.loan.data.dto.request.LoanRequest;
import com.loan.hero.loan.data.dto.response.LoanDTO;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan.data.models.LoanDocuments;
import com.loan.hero.loan.data.models.LoanStatus;
import com.loan.hero.loan.service.LoanService;
import com.loan.hero.notification.InitToken;
import com.loan.hero.notification.dto.EmailRequest;
import com.loan.hero.notification.dto.MailInfo;
import com.loan.hero.notification.interfaces.InitTokenService;
import com.loan.hero.notification.interfaces.MailService;
import lombok.AllArgsConstructor;
import org.hibernate.mapping.Collection;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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

import static com.loan.hero.auth.user.data.models.Role.COSTUMER;
import static com.loan.hero.customer.data.models.enums.AgreementDecision.ACCEPT;
import static com.loan.hero.customer.data.models.enums.AgreementDecision.REJECT;
import static com.loan.hero.loan.data.models.LoanStatus.ACTIVE;
import static com.loan.hero.loan.data.models.LoanStatus.CLOSED;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final LoanAgreementService loanAgreementService;
    private final CustomerRepository customerRepository;
    private final HeroTokenService heroTokenService;
    private final InitTokenService initTokenService;
    private final PasswordEncoder passwordEncoder;
    private final TemplateEngine templateEngine;
    private final CloudService cloudService;
    private final LoanService loanService;
    private final UserService userService;
    private final MailService mailService;
    private final JwtService jwtService;

    @Override
    public InitResponse initAccess(InitRequest initRequest) {
        if (customerRepository.existsByUserEmail(initRequest.getEmail())) {
            return InitResponse.LOGIN;
        }
        String emailResponse = sendSignUpMail(initRequest.getEmail());
        if (emailResponse != null)
            return InitResponse.SIGNUP;
        throw new HeroException("Registration failed");
    }

    private String sendSignUpMail(String email) {
        final String token = HeroUtilities.generateToken(7);
        initTokenService.saveToken(
                InitToken.builder()
                        .token(token)
                        .email(email)
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
                .to(Collections.singletonList(new MailInfo(username, email)))
                .subject("Welcome to Hero Money")
                .htmlContent(content)
                .build();
        return mailService.sendMail(emailRequest);
    }

    @Override
    public AuthenticationToken register(SignUpRequest signUpRequest) {
        final InitToken initToken = initTokenService.findByTokenAndEmail(
                signUpRequest.getToken(),
                signUpRequest.getEmail()
        ).orElseThrow(HeroException::new);
        int age = getAge(signUpRequest.getDateOfBirth());
        if (!initTokenService.isValid(initToken) || age < 18) {
            throw new HeroException("Invalid credentials");
        }

        final User user = User.builder()
                .firstName(signUpRequest.getFirstName().trim())
                .lastName(signUpRequest.getLastName().trim())
                .email(signUpRequest.getEmail().trim())
                .password(passwordEncoder.encode(signUpRequest.getPassword().trim()))
                .enabled(true)
                .roles(Set.of(COSTUMER))
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
    public Customer getCurrentCustomer() {
        return customerRepository.findByUser(currentUser())
                .orElseThrow(UserNotFoundException::new);
    }

    private User currentUser() {
        return userService.getCurrentUser();
    }

    @Override
    public String uploadCustomerImage(MultipartFile image) {
        final Customer customer = getCurrentCustomer();
        try {
            final String imageUrl = cloudService.uploadFile(image);
            customer.getUser().setUserImage(imageUrl);
            customerRepository.save(customer);
            return "Image uploaded successfully";
        } catch (RuntimeException e) {
            throw new HeroException("Image upload failed");
        }
    }

    @Override
    public LoanDTO apply(LoanRequest request) {
        final Customer customer = getCurrentCustomer();
        if (!customer.isComplete()) {
            throw new HeroException("Complete profile update");
        }
        final LoanDocuments loanDocuments = LoanDocuments.builder()
                .paySlip(uploadImage(request.getPaySlip()))
                .bankStatement(uploadImage(request.getAccountStatement()))
                .build();

        final Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(request.getLoanAMount())
                .loanPurpose(request.getLoanPurpose())
                .loanDocuments(loanDocuments)
                .paymentFrequency(request.getPaymentFrequency())
                .repaymentTerm(request.getRepaymentTerm())
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
    public LoanStatus viewLoanStatus(Long loanId) {
        final Loan loan = loanService.findById(loanId);
        return loan.getLoanStatus();
    }

    @Override
    public Map<String, String> allLoansStatus() {
        final List<Loan> allLoansByCustomer = loanService.allLoansByCustomerId(
                getCurrentCustomer().getId()
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
    public LoanAgreement viewAgreement(Long agreementId) {
        return loanAgreementService.findById(agreementId);
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
        try {
            return cloudService.uploadFile(file);
        } catch (RuntimeException e) {
            throw new HeroException("Image upload failed");
        }
    }

    @Override
    public Customer updateCustomerProfile(UpdateCustomerRequest request) {
        final Customer customer = getCurrentCustomer();
        final User user = updateUser(request, customer);

        customer.setUser(user);
        customer.setGender(request.getGender());
        customer.setSalary(request.getSalary());
        customer.setCompanyName(request.getCompanyName());
        customer.setGender(request.getGender());
        customer.setJobStatus(request.getJobStatus());
        customer.setMaritalStatus(request.getMaritalStatus());
        customer.setFormOfIdentity(uploadImage(request.getFormOfIdentity()));
        customer.setComplete(true);
        return customerRepository.save(customer);
    }

    @Override
    public AgreementDecision agreementDecision(Decision decision) {
        final LoanAgreement loanAgreement = loanAgreementService.findById(decision.getLoanAgreementId());
        switch (decision.getAgreementDecision()) {
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
        throw new HeroException();
    }

    private User updateUser(UpdateCustomerRequest request, Customer customer) {
       final User user = customer.getUser();
        user.setUserImage(uploadImage(request.getUserImage()));
        user.setPhoneNumber(request.getPhoneNumber());
        user.setAddress(
                Address.builder()
                        .houseNumber(request.getHouseNumber())
                        .streetName(request.getStreetName())
                        .city(request.getCity())
                        .state(request.getState())
                        .state(request.getState())
                        .zipCode(request.getZipCode())
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
