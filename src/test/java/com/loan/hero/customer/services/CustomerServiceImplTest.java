package com.loan.hero.customer.services;

import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.request.InitRequest;
import com.loan.hero.customer.data.dto.request.SignUpRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.exceptions.HeroException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
class CustomerServiceImplTest {

    @Autowired
    private CustomerService customerService;

    @BeforeEach
    void setUp() {
    }

    @Test
    void initAccess() {
        InitRequest initRequest = InitRequest.builder()
                .email("qem06gpzxa@klovenode.com")
                .build();
        InitResponse response = customerService.initAccess(initRequest);
        assertThat(response).isNotNull().isEqualTo(InitResponse.SIGNUP);
    }

    @Test
    void register() {
        SignUpRequest request = SignUpRequest.builder()
                .token("GX7CkKmH6Q")
                .firstName("Michael")
                .lastName("Dean")
                .email("bjomn6mfyz@greencafe24.com")
                .password("Pass!234")
                .dateOfBirth("12/09/1990")
                .build();
        AuthenticationToken token = customerService.register(request);
        assertThat(token)
                .isNotNull()
                .isInstanceOf(AuthenticationToken.class);
    }
    @Test
    void registerThrowsExceptionWhenAlreadyRegistered() {
        SignUpRequest request = SignUpRequest.builder()
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
        SignUpRequest request = SignUpRequest.builder()
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
        SignUpRequest request = SignUpRequest.builder()
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
        MockMultipartFile file =
                new MockMultipartFile(
                        "myself",
                        new FileInputStream("C:\\Users\\Dean\\hero\\src\\main\\resources\\p1wbidsbjpnwgtcdtla8.jpg")
                );
       String response = customerService.uploadCustomerImage(file);
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