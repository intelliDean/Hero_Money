package com.api.xpress.gateways;

import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.user.CurrentUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.customer.data.dto.request.Decision;
import com.api.xpress.customer.data.dto.request.InitRequest;
import com.api.xpress.customer.data.dto.request.SignUpRequest;
import com.api.xpress.customer.data.dto.request.UpdateCustomerRequest;
import com.api.xpress.customer.data.models.enums.AgreementDecision;
import com.api.xpress.customer.data.dto.response.InitResponse;
import com.api.xpress.customer.data.models.Customer;
import com.api.xpress.customer.services.CustomerService;
import com.api.xpress.loan.data.dto.request.LoanRequest;
import com.api.xpress.loan.data.dto.response.LoanDTO;
import com.api.xpress.loan.data.models.LoanStatus;
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

    @PostMapping("/init")
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
            value = "/image",
            consumes = MULTIPART_FORM_DATA_VALUE
    )
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Upload customer image")
    public ResponseEntity<String> uploadCustomerImage(
            @ModelAttribute MultipartFile file, @CurrentUser AuthenticatedUser currentUser
    ) {
        return ResponseEntity.ok(
                customerService.uploadCustomerImage(file, currentUser)
        );
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get current customer")
    public ResponseEntity<Customer> getCurrentCustomer(@CurrentUser AuthenticatedUser currentUser) {
        return ResponseEntity.ok(
                customerService.getCurrentCustomer(currentUser)
        );
    }

    @PostMapping(value = "/update", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Complete customer profile")
    public ResponseEntity<Customer> updateCustomerProfile(
            @ModelAttribute @Valid UpdateCustomerRequest request, @CurrentUser AuthenticatedUser currentUser
    ) {
        return ResponseEntity.ok(customerService.updateCustomerProfile(request, currentUser));
    }

    @PostMapping(value = "/apply", consumes = MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Customer apply for loan")
    public ResponseEntity<LoanDTO> applyForLoan(
            @ModelAttribute @Valid LoanRequest request, @CurrentUser AuthenticatedUser currentUser) {

        return ResponseEntity.ok(customerService.apply(request, currentUser));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAnyAuthority('LOAN_OFFICER', 'CUSTOMER')")
    @Operation(summary = "Get the status of a loan application")
    public ResponseEntity<LoanStatus> getLoanStatus(@ParameterObject Long loanId, @CurrentUser AuthenticatedUser currentUser) {

        return ResponseEntity.ok(customerService.viewLoanStatus(loanId, currentUser));
    }

    @GetMapping("/all_status")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Get all status of a customer loan application")
    public ResponseEntity<Map<String, String>> getAllLoanStatus(@CurrentUser AuthenticatedUser currentUser) {

        return ResponseEntity.ok(customerService.allLoansStatus(currentUser));
    }

    @GetMapping("/agreement")
    @Operation(summary = "To view agreement")
    @PreAuthorize("hasAnyAuthority('LOAN_OFFICER', 'CUSTOMER')")
    public ResponseEntity<LoanAgreement> viewAgreement(@ParameterObject Long agreementId, @CurrentUser AuthenticatedUser currentUser) {

        return ResponseEntity.ok(customerService.viewAgreement(agreementId, currentUser));
    }

    @PostMapping("/decision")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    @Operation(summary = "Accept or reject agreement")
    public ResponseEntity<AgreementDecision> agreementDecision(@RequestBody @Valid Decision decision, @CurrentUser AuthenticatedUser currentUser) {

        return ResponseEntity.ok(customerService.agreementDecision(decision, currentUser));
    }
}
