package com.loan.hero.customer.services;

import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.request.InitRequest;
import com.loan.hero.customer.data.dto.request.SignUpRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.exceptions.HeroException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;
//    private final CustomerRepository customerRepository = mock(CustomerRepository.class);
//    private final LoanAgreementService loanAgreementService = mock(LoanAgreementService.class);
//    private final HeroTokenService heroTokenService = mock(HeroTokenService.class);
//    private final InitTokenService initTokenService = mock(InitTokenService.class);
//    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
//    private final TemplateEngine templateEngine = mock(TemplateEngine.class);
//    private final CloudService cloudService = mock(CloudService.class);
//    private final LoanService loanService = mock(LoanService.class);
//    private final UserService userService = mock(UserService.class);
//    private final MailService mailService = mock(MailService.class);
//    private final JwtService jwtService = mock(JwtService.class);
//    private CustomerService customerService;


//    @BeforeEach
//    void setUp() {
//        customerService = new CustomerServiceImpl(
//                loanAgreementService, customerRepository, heroTokenService, initTokenService,
//                passwordEncoder, templateEngine, cloudService, loanService, userService,
//                mailService, jwtService
//        );
//    }

    @Test
    void ifEmailExistInDBReturnLogin() {
        String email = "qem06gpzxa@klovenode.com";

        final InitRequest initRequest = InitRequest.builder().email(email).build();
       // when(customerRepository.existsByUserEmail(email)).thenReturn(true);
        final InitResponse response = customerService.initAccess(initRequest);

        assertThat(response).isNotNull().isEqualTo(InitResponse.SIGNUP);
    }
//    @Test
//    void ifEmailNotExistInDBReturnSignUp() {
//        String email = "qem06gpzxa@klovenode.com";
//        EmailRequest emailRequest = mock(EmailRequest.class);
//        final String username = "dean";
//        final String token = "dean123";
//        final Context context = new Context();
//        context.setVariables(
//                Map.of(
//                        "username", username,
//                        "token", token
//                )
//        );
//
//
//
//        final InitRequest initRequest = InitRequest.builder().email(email).build();
//        when(customerRepository.existsByUserEmail(email)).thenReturn(false);
//        //when(templateEngine.process("customer_mail", context)).thenReturn("");
//        when(mailService.sendMail(emailRequest)).thenReturn("");
//
//        final InitResponse response = customerService.initAccess(initRequest);
//
//        assertThat(response).isNotNull().isEqualTo(InitResponse.SIGNUP);
//    }
//    @Test
//    void initAccess() {
//        InitRequest request = Mockito.mock(InitRequest.class);
//        final InitRequest initRequest = InitRequest.builder()
//                .email("qem06gpzxa@klovenode.com")
//                .build();
//        final InitResponse response = customerService.initAccess(initRequest);
//        assertThat(response).isNotNull().isEqualTo(InitResponse.SIGNUP);
//    }

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
        ).isInstanceOf(HeroException.class)
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
        ).isInstanceOf(HeroException.class)
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
                        new FileInputStream("C:\\Users\\Dean\\hero\\src\\main\\resources\\p1wbidsbjpnwgtcdtla8.jpg")
                );
       final String response = customerService.uploadCustomerImage(file);
       assertThat(response).isNotNull()
               .isInstanceOf(String.class)
               .isEqualTo("Image uploaded successfully");
    }

    @Test
    void apply() {
    }

    @Test
    void viewLoanStatus() {
    }

    @Test
    void allLoansStatus() {
    }

    @Test
    void viewAgreement() {
    }

    @Test
    void updateCustomerProfile() {
    }

    @Test
    void agreementDecision() {
    }
}