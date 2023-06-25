package com.loan.hero.customer.controller;

import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.customer.data.dto.request.Decision;
import com.loan.hero.customer.data.dto.request.InitRequest;
import com.loan.hero.customer.data.dto.request.SignUpRequest;
import com.loan.hero.customer.data.dto.request.UpdateCustomerRequest;
import com.loan.hero.customer.data.models.enums.AgreementDecision;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.customer.services.CustomerService;
import com.loan.hero.loan.data.dto.request.LoanRequest;
import com.loan.hero.loan.data.dto.response.LoanDTO;
import com.loan.hero.loan.data.models.LoanStatus;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@AllArgsConstructor
@Tag(name = "Customer Controller")
@RequestMapping("/api/v1/customer")
public class CustomerController {
    private final CustomerService customerService;

    @PostMapping("init")
    @Operation(summary = "Beginning of signup")
    public ResponseEntity<InitResponse> initAccess(
            @RequestBody @Valid InitRequest initRequest
    ) {
        return ResponseEntity.ok(
                customerService.initAccess(initRequest)
        );
    }

    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<AuthenticationToken> register(
            @RequestBody @Valid SignUpRequest signUpRequest
    ) {
        return ResponseEntity.ok(
                customerService.register(signUpRequest)
        );
    }

    @PostMapping(
            value = "image",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAuthority('COSTUMER')")
    @Operation(summary = "Upload customer image")
    public ResponseEntity<String> uploadCustomerImage(
            @ModelAttribute MultipartFile file
    ) {
        return ResponseEntity.ok(
                customerService.uploadCustomerImage(file)
        );
    }

    @GetMapping
    @PreAuthorize("hasAuthority('COSTUMER')")
    @Operation(summary = "Get current customer")
    public ResponseEntity<Customer> getCurrentCustomer() {
        return ResponseEntity.ok(
                customerService.getCurrentCustomer()
        );
    }

    @PostMapping(
            value = "update",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAuthority('COSTUMER')")
    @Operation(summary = "Complete customer profile")
    public ResponseEntity<Customer> updateCustomerProfile(
            @ModelAttribute @Valid UpdateCustomerRequest request
    ) {
        return ResponseEntity.ok(
                customerService.updateCustomerProfile(request)
        );
    }

    @PostMapping(
            value = "apply",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAuthority('COSTUMER')")
    @Operation(summary = "Customer apply for loan")
    public ResponseEntity<LoanDTO> applyForLoan(
            @ModelAttribute @Valid LoanRequest request
    ) {
        return ResponseEntity.ok(
                customerService.apply(request)
        );
    }

    @GetMapping("status")
    @PreAuthorize("hasAnyAuthority('LOAN_OFFICER', 'COSTUMER')")
    @Operation(summary = "Get the status of a loan application")
    public ResponseEntity<LoanStatus> getLoanStatus(
            @ParameterObject Long loanId
    ) {
        return ResponseEntity.ok(
                customerService.viewLoanStatus(loanId)
        );
    }
    @GetMapping("all_status")
    @PreAuthorize("hasAuthority('COSTUMER')")
    @Operation(summary = "Get all status of a customer loan application")
    public ResponseEntity<Map<String, String>> getAllLoanStatus() {
        return ResponseEntity.ok(
                customerService.allLoansStatus()
        );
    }

    @GetMapping("agreement")
    @Operation(summary = "To view agreement")
    @PreAuthorize("hasAnyAuthority('LOAN_OFFICER', 'COSTUMER')")
    public ResponseEntity<LoanAgreement> viewAgreement(
            @ParameterObject Long agreementId
    ) {
        return ResponseEntity.ok(
                customerService.viewAgreement(agreementId)
        );
    }

    @PostMapping("decision")
    @PreAuthorize("hasAuthority('COSTUMER')")
    @Operation(summary = "Accept or reject agreement")
    public ResponseEntity<AgreementDecision> agreementDecision(
            @RequestBody @Valid Decision decision
    ) {
        return ResponseEntity.ok(
                customerService.agreementDecision(decision)
        );
    }
}
