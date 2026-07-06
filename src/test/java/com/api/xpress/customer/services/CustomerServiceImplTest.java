package com.api.xpress.customer.services;

import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.customer.data.dto.request.InitRequest;
import com.api.xpress.customer.data.dto.request.SignUpRequest;
import com.api.xpress.customer.data.dto.response.InitResponse;
import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.auth.user.data.repositories.UserRepository;
import com.api.xpress.customer.data.models.Customer;
import com.api.xpress.customer.data.repositories.CustomerRepository;
import com.api.xpress.exceptions.XpressException;
import com.api.xpress.exceptions.UserNotAuthorizedException;
import com.api.xpress.notification.InitToken;
import com.api.xpress.notification.InitTokenRepository;
import com.api.xpress.notification.interfaces.InitTokenService;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan.data.models.LoanStatus;
import com.api.xpress.loan.data.repository.LoanRepository;
import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.agreement.data.repository.LoanAgreementRepository;
import com.api.xpress.customer.data.dto.request.Decision;
import com.api.xpress.customer.data.models.enums.AgreementDecision;
import com.api.xpress.loan_officer.data.models.LoanOfficer;
import com.api.xpress.loan_officer.data.repositories.LoanOfficeRepository;
import com.api.xpress.auth.user.data.models.Role;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private InitTokenService initTokenService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private InitTokenRepository initTokenRepository;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private LoanAgreementRepository loanAgreementRepository;

    @Autowired
    private LoanOfficeRepository loanOfficeRepository;

    @BeforeEach
    void setUp() {
        loanAgreementRepository.deleteAll();
        loanOfficeRepository.deleteAll();
        loanRepository.deleteAll();
        customerRepository.deleteAll();
        userRepository.deleteAll();
        initTokenRepository.deleteAll();

        initTokenService.saveToken(
                InitToken.builder()
                        .token("GX7CkKmH6Q")
                        .email("bjomn6mfyz@greencafe24.com")
                        .expireAt(LocalDateTime.now().plusHours(3))
                        .build()
        );
        initTokenService.saveToken(
                InitToken.builder()
                        .token("8x4iV1hzWw")
                        .email("qem06gpzxa@klovenode.com")
                        .expireAt(LocalDateTime.now().plusHours(3))
                        .build()
        );
    }

    @Test
    void initAccess() {
        final InitRequest initRequest = InitRequest.builder()
                .email("qem06gpzxa@klovenode.com")
                .build();
        final InitResponse response = customerService.initAccess(initRequest);
        assertThat(response).isNotNull().isEqualTo(InitResponse.SIGNUP);
    }

    @Test
    void register() {
        final SignUpRequest request = SignUpRequest.builder()
                .token("GX7CkKmH6Q")
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("Pass!234")
                .dateOfBirth("12/09/1990")
                .build();
        final AuthenticationToken token = customerService.register(request);
        assertThat(token)
                .isNotNull()
                .isInstanceOf(AuthenticationToken.class);
    }
    @Test
    void registerThrowsExceptionWhenAlreadyRegistered() {
       final SignUpRequest request = SignUpRequest.builder()
                .token("GX7CkKmH6Q")
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("Pass!234")
                .dateOfBirth("12/09/1990")
                .build();
        customerService.register(request);
        assertThatThrownBy(
                () -> {
                    customerService.register(request);
                }
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("An error occurred");
    }
    @Test
    void under18CannotRegister() {
        final SignUpRequest request = SignUpRequest.builder()
                .token("8x4iV1hzWw")
                .firstName("Michael")
                .lastName("Dean")
                .email("qem06gpzxa@klovenode.com")
                .password("Pass!234")
                .dateOfBirth("12/09/2021")
                .build();
        assertThatThrownBy(
                () -> {
                    customerService.register(request);
                }
        ).isInstanceOf(XpressException.class)
                .hasMessageContaining("Invalid credentials");
    }
    @Test
    void invalidTokenThrowsException() {
        final SignUpRequest request = SignUpRequest.builder()
                .token("GX7CkKmH6Q")
                .firstName("Michael")
                .lastName("Dean")
                .email("qem06gpzxa@klovenode.com")
                .password("Pass!234")
                .dateOfBirth("12/09/1990")
                .build();
        assertThatThrownBy(
                () -> {
                    customerService.register(request);
                }
        ).isInstanceOf(XpressException.class)
                .hasMessageContaining("An error occurred");
    }


    @Test
    void getCurrentCustomer() {
    }

    @Test
    void uploadCustomerImage() throws IOException {
        final MockMultipartFile file =
                new MockMultipartFile(
                        "myself",
                        "p1wbidsbjpnwgtcdtla8.jpg",
                        "image/jpeg",
                        new FileInputStream("src/main/resources/p1wbidsbjpnwgtcdtla8.jpg")
                );

        final User user = User.builder()
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("password")
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();

        final Customer customer = Customer.builder()
                .user(user)
                .age(30)
                .complete(false)
                .build();
        customerRepository.save(customer);

        AuthenticatedUser currentUser = AuthenticatedUser.builder()
                .user(user)
                .build();
       final String response = customerService.uploadCustomerImage(file, currentUser);
       assertThat(response).isNotNull()
               .isInstanceOf(String.class)
               .isEqualTo("Image uploaded successfully");
    }

    @Test
    void apply() {
    }

    @Test
    void viewLoanStatus() {
        // Create user 1 (owner of loan) but do not save user1 separately
        final User user1 = User.builder()
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("password")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final Customer customer1 = Customer.builder()
                .user(user1)
                .age(30)
                .complete(true)
                .build();

        // Create customer 2 and user 2 but do not save user2 separately
        final User user2 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("other@domain.com")
                .password("password")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final Customer customer2 = customerRepository.save(Customer.builder()
                .user(user2)
                .age(28)
                .complete(true)
                .build());

        // Create and save loan for customer 1 (cascades save customer1 and user1)
        final Loan loan = loanRepository.save(Loan.builder()
                .customer(customer1)
                .loanAmount(java.math.BigDecimal.valueOf(5000))
                .loanStatus(LoanStatus.PENDING)
                .repaymentTerm(12)
                .build());

        // Set authenticated user as customer 1 (should succeed)
        AuthenticatedUser currentOwner = AuthenticatedUser.builder().user(loan.getCustomer().getUser()).build();
        LoanStatus status = customerService.viewLoanStatus(loan.getId(), currentOwner);
        assertThat(status).isEqualTo(LoanStatus.PENDING);

        // Set authenticated user as customer 2 (should fail with IDOR block)
        AuthenticatedUser intruder = AuthenticatedUser.builder().user(customer2.getUser()).build();
        assertThatThrownBy(() -> customerService.viewLoanStatus(loan.getId(), intruder))
                .isInstanceOf(UserNotAuthorizedException.class);
    }

    @Test
    void allLoansStatus() {
    }

    @Test
    void viewAgreement() {
        // Create customer 1 and user 1
        final User user1 = User.builder()
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("password")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final Customer customer1 = Customer.builder()
                .user(user1)
                .age(30)
                .complete(true)
                .build();

        // Create customer 2 and user 2
        final User user2 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("other@domain.com")
                .password("password")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final Customer customer2 = customerRepository.save(Customer.builder()
                .user(user2)
                .age(28)
                .complete(true)
                .build());

        // Create loan officer
        final User officerUser = User.builder()
                .firstName("Officer")
                .lastName("Bob")
                .email("officer1@xpress.com")
                .password("password")
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final LoanOfficer officer = LoanOfficer.builder()
                .user(officerUser)
                .employeeId("OFF-001")
                .build();

        // Create loan
        final Loan loan = Loan.builder()
                .customer(customer1)
                .loanAmount(java.math.BigDecimal.valueOf(5000))
                .loanStatus(LoanStatus.PENDING)
                .repaymentTerm(12)
                .interestRate(java.math.BigDecimal.valueOf(5.0))
                .build();

        // Save loan agreement (cascades save loan, customer1, user1, officer, officerUser)
        final LoanAgreement agreement = loanAgreementRepository.save(LoanAgreement.builder()
                .loan(loan)
                .loanOfficer(officer)
                .agreed(false)
                .build());

        // Set authenticated user as customer 1 (should succeed)
        AuthenticatedUser currentOwner = AuthenticatedUser.builder().user(agreement.getLoan().getCustomer().getUser()).build();
        LoanAgreement result = customerService.viewAgreement(agreement.getId(), currentOwner);
        assertThat(result).isNotNull();

        // Set authenticated user as customer 2 (should fail with IDOR block)
        AuthenticatedUser intruder = AuthenticatedUser.builder().user(customer2.getUser()).build();
        assertThatThrownBy(() -> customerService.viewAgreement(agreement.getId(), intruder))
                .isInstanceOf(UserNotAuthorizedException.class);
    }

    @Test
    void updateCustomerProfile() {
    }

    @Test
    void agreementDecision() {
        // Create customer 1 and user 1
        final User user1 = User.builder()
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("password")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final Customer customer1 = Customer.builder()
                .user(user1)
                .age(30)
                .complete(true)
                .build();

        // Create customer 2 and user 2
        final User user2 = User.builder()
                .firstName("John")
                .lastName("Doe")
                .email("other@domain.com")
                .password("password")
                .roles(Set.of(Role.CUSTOMER))
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final Customer customer2 = customerRepository.save(Customer.builder()
                .user(user2)
                .age(28)
                .complete(true)
                .build());

        // Create loan officer
        final User officerUser = User.builder()
                .firstName("Officer")
                .lastName("Bob")
                .email("officer2@xpress.com")
                .password("password")
                .enabled(true)
                .registeredAt(LocalDateTime.now())
                .build();
        final LoanOfficer officer = LoanOfficer.builder()
                .user(officerUser)
                .employeeId("OFF-002")
                .build();

        // Create loan
        final Loan loan = Loan.builder()
                .customer(customer1)
                .loanAmount(java.math.BigDecimal.valueOf(5000))
                .loanStatus(LoanStatus.PENDING)
                .repaymentTerm(12)
                .interestRate(java.math.BigDecimal.valueOf(5.0))
                .build();

        // Save loan agreement (cascades save loan, customer1, user1, officer, officerUser)
        final LoanAgreement agreement = loanAgreementRepository.save(LoanAgreement.builder()
                .loan(loan)
                .loanOfficer(officer)
                .agreed(false)
                .build());

        // Set authenticated user as customer 1 (should succeed in accepting)
        AuthenticatedUser currentOwner = AuthenticatedUser.builder().user(agreement.getLoan().getCustomer().getUser()).build();
        Decision decision = Decision.builder()
                .loanAgreementId(agreement.getId())
                .agreementDecision(AgreementDecision.ACCEPT)
                .build();
        AgreementDecision response = customerService.agreementDecision(decision, currentOwner);
        assertThat(response).isEqualTo(AgreementDecision.ACCEPT);

        // Set authenticated user as customer 2 (should fail in deciding)
        AuthenticatedUser intruder = AuthenticatedUser.builder().user(customer2.getUser()).build();
        assertThatThrownBy(() -> customerService.agreementDecision(decision, intruder))
                .isInstanceOf(UserNotAuthorizedException.class);
    }
}