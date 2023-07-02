package com.loan.hero.customer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.hero.customer.data.dto.request.InitRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();
    void initAccess() throws Exception {
        final InitRequest request = InitRequest.builder()
                .email("6wif2nzxlj@klovenode.com")
                .build();
        final String jsonRequest = objectMapper.writeValueAsString(request);
        mockMvc.perform(post("/api/v1/customer/init")
                    .content(jsonRequest)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().is(HttpStatus.OK.value()))
                    .andDo(print());

    }

    @Test
    void register() {
    }

    @Test
    void uploadCustomerImage() {
    }

    @Test
    void getCurrentCustomer() {
    }

    @Test
    void updateCustomerProfile() {
    }

    @Test
    void applyForLoan() {
    }

    @Test
    void getLoanStatus() {
    }

    @Test
    void getAllLoanStatus() {
    }

    @Test
    void viewAgreement() {
    }

    @Test
    void agreementDecision() {
    }
}