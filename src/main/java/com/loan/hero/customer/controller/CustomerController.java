package com.loan.hero.customer.controller;

import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.InitRequest;
import com.loan.hero.customer.data.dto.SignUpRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.services.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name= "Customer Controller")
@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("init")
    @Operation(summary = "Beginning of signup")
    public ResponseEntity<InitResponse> initAccess(@RequestParam InitRequest initRequest) {
        return ResponseEntity.ok(
                customerService.initAccess(initRequest)
        );
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<AuthenticationToken> register(@RequestBody SignUpRequest signUpRequest) {
        return ResponseEntity.ok(
                customerService.register(signUpRequest)
        );
    }
}
