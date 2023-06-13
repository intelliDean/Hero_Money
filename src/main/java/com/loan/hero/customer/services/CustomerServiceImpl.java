package com.loan.hero.customer.services;

import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.auth.security.utility.JwtService;
import com.loan.hero.auth.user.data.models.Address;
import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.auth.user.service.HeroTokenService;
import com.loan.hero.auth.user.service.UserService;
import com.loan.hero.cloud_service.CloudService;
import com.loan.hero.customer.data.dto.*;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.customer.data.repositories.CustomerRepository;
import com.loan.hero.exceptions.HeroException;
import com.loan.hero.exceptions.UserNotFoundException;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.loan.hero.auth.user.data.models.Role.COSTUMER;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final HeroTokenService heroTokenService;
    private final InitTokenService initTokenService;
    private final LoanService loanService;
    private final MailService mailService;
    private final TemplateEngine templateEngine;
    private final UserService userService;
    private final CloudService cloudService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public InitResponse initAccess(InitRequest initRequest) {
        if (customerRepository.existsByUserEmail(initRequest.getEmail())) {
            return InitResponse.LOGIN;
        }
        String emailResponse = initTokenService.sendSignUpMail(initRequest.getEmail());
        if (emailResponse != null)
            return InitResponse.SIGNUP;
        throw new HeroException("Registration failed");
    }

    @Override
    public AuthenticationToken register(SignUpRequest signUpRequest) {
        InitToken initToken = initTokenService.findByTokenAndEmail(
                signUpRequest.getToken(),
                signUpRequest.getEmail()
        ).orElseThrow(HeroException::new);
        int age = getAge(signUpRequest.getDateOfBirth());
        if (!initTokenService.isValid(initToken) || age < 18) {
            throw new HeroException("Invalid credentials");
        }

        User user = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .enabled(true)
                .roles(Set.of(COSTUMER))
                .build();

        Customer customer = Customer.builder()
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
        LocalDate birthDate = LocalDate.parse(
                dateOfBirth,
                DateTimeFormatter.ofPattern("dd/MM/yyyy")
        );

        Period period = Period.between(
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
        Customer customer = getCurrentCustomer();
        try {
            String imageUrl = cloudService.uploadImage(image);
            customer.getUser().setUserImage(imageUrl);
            customerRepository.save(customer);
            return "Image uploaded successfully";
        } catch (RuntimeException e) {
            throw new HeroException("Image upload failed");
        }
    }

    @Override
    public LoanDTO apply(LoanRequest request) {
        Customer customer = getCurrentCustomer();
        if (!customer.isComplete()) {
           throw new HeroException("Update your profile");
        }
        LoanDocuments loanDocuments = LoanDocuments.builder()
                .paySlip(uploadImage(request.getPaySlip()))
                .bankStatement(uploadImage(request.getAccountStatement()))
                .build();

        Loan loan = Loan.builder()
                .customer(customer)
                .loanAmount(request.getLoanAMount())
                .loanPurpose(request.getLoanPurpose())
                .loanDocuments(loanDocuments)
                .paymentFrequency(request.getPaymentFrequency())
                .applicationDate(LocalDateTime.now())
                .loanStatus(LoanStatus.PENDING)
                .build();
        Loan savedLoan = loanService.saveLoan(loan);

        sendMail(customer, savedLoan);

        return LoanDTO.builder()
                .message("Your application was successful.\nPlease check your email")
                .applicationDate(savedLoan.getApplicationDate())
                .loanStatus(savedLoan.getLoanStatus())
                .build();
    }

    private void sendMail(Customer customer, Loan savedLoan) {
        String name = customer.getUser().getFirstName();
        Context context = new Context();
        context.setVariables(
                Map.of(
                        "name", name,
                        "amount", savedLoan.getLoanAmount().toString(),
                        "status", savedLoan.getLoanStatus().name()
                )
        );
        String content = templateEngine.process("loan_application_mail", context);
        EmailRequest emailRequest = EmailRequest.builder()
                .to(List.of(new MailInfo(name, customer.getUser().getEmail())))
                .subject("Application Update")
                .htmlContent(content)
                .build();

        mailService.sendMail(emailRequest);
    }

    private String uploadImage(MultipartFile file) {
        try {
            return cloudService.uploadImage(file);
        } catch (RuntimeException e) {
            throw new HeroException("Image upload failed");
        }
    }

    @Override
    public Customer updateCustomerProfile(UpdateCustomerRequest request) {
        Customer customer = getCurrentCustomer();
        User user = updateUser(request, customer);

        customer.setUser(user);
        customer.setGender(request.getGender());
        customer.setSalary(request.getSalary());
        customer.setCompanyName(request.getCompanyName());
        customer.setGender(request.getGender());
        customer.setJobStatus(request.getJobStatus());
        customer.setMaritalStatus(request.getMaritalStatus());
       // customer.setPaySlip(uploadImage(request.getPaySlip()));
       // customer.setAccountStatement(uploadImage(request.getAccountStatement()));
        customer.setFormOfIdentity(uploadImage(request.getFormOfIdentity()));
        customer.setComplete(true);
        return customerRepository.save(customer);

//        return new ModelMapper().map(
//                customer,
//                CustomerDTO.class
//        );
    }

    private User updateUser(UpdateCustomerRequest request, Customer customer) {
        User user = customer.getUser();
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
        String email = user.getEmail();
        String accessToken = jwtService.generateAccessToken(
                getUserAuthority(user),
                email
        );
        String refreshToken = jwtService.generateRefreshToken(email);

        HeroToken heroToken = HeroToken.builder()
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
