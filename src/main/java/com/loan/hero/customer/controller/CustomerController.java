package com.loan.hero.customer.controller;

import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.CustomerDTO;
import com.loan.hero.customer.data.dto.InitRequest;
import com.loan.hero.customer.data.dto.SignUpRequest;
import com.loan.hero.customer.data.dto.UpdateCustomerRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.customer.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@AllArgsConstructor
@Tag(name = "Customer Controller")
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("init")
    @Operation(summary = "Beginning of signup")
    public ResponseEntity<InitResponse> initAccess(
            @ParameterObject InitRequest initRequest
    ) {
        return ResponseEntity.ok(
                customerService.initAccess(initRequest)
        );
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<AuthenticationToken> register(
            @ParameterObject SignUpRequest signUpRequest
    ) {
        return ResponseEntity.ok(
                customerService.register(signUpRequest)
        );
    }

    @PostMapping(
            value = "image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Upload customer image")
    public ResponseEntity<String> uploadCustomerImage(
            @RequestParam MultipartFile file
    ) {
        return ResponseEntity.ok(
                customerService.uploadCustomerImage(file)
        );
    }

    @GetMapping
    @Operation(summary = "Get current customer")
    public ResponseEntity<Customer> getCurrentCustomer() {
        return ResponseEntity.ok(
                customerService.getCurrentCustomer()
        );
    }

    @PostMapping(
            value = "update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(summary = "Complete customer profile")
    public ResponseEntity<Customer> updateCustomerProfile(
            @ModelAttribute UpdateCustomerRequest request
    ) {
        return ResponseEntity.ok(
                customerService.updateCustomerProfile(request)
        );
    }
}
