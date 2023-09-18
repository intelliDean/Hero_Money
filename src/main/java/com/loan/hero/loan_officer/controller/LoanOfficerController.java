package com.loan.hero.loan_officer.controller;

import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan.data.models.LoanStatus;
import com.loan.hero.loan_officer.data.dto.request.AgreementRequest;
import com.loan.hero.loan_officer.data.dto.request.InviteRequest;
import com.loan.hero.loan_officer.data.dto.request.OfficerRequest;
import com.loan.hero.loan_officer.data.dto.request.UpdateLoanRequest;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import com.loan.hero.loan_officer.services.LoanOfficerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;


@RestController
@AllArgsConstructor
@RequestMapping("/api/v1/officer")
@Tag(name = "Loan Officer Controller")
public class LoanOfficerController {
    private final LoanOfficerService loanOfficerService;


    @PostMapping(value = "update", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Update Loan Officer profile")
    public ResponseEntity<AuthenticationToken> updateLoanOfficer(
            @ModelAttribute @Valid OfficerRequest request
    ) {
        return ResponseEntity.ok(
                loanOfficerService.completeOfficerProfile(request)
        );
    }

    @PostMapping("update_loan")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "Update the status of a loan application")
    public ResponseEntity<LoanStatus> updateLoanStatus(
            @RequestBody @Valid UpdateLoanRequest request
    ) {
        return ResponseEntity.ok(
                loanOfficerService.updateLoanStatus(request)
        );
    }

    @GetMapping("fresh")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "Pending applications")
    public ResponseEntity<Page<Loan>> fresh(
            @ParameterObject int pageNumber
    ) {
        return ResponseEntity.ok(
                loanOfficerService.allFreshLoans(pageNumber)
        );
    }

    @GetMapping("current")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "Get current loan officer")
    public ResponseEntity<LoanOfficer> currentLoanOfficer() {
        return ResponseEntity.ok(
                loanOfficerService.cuurentLoanOfficer()
        );
    }

    @PostMapping("approve")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "To approve a loan application")
    public ResponseEntity<LoanStatus> approveLoanApplication(
            @ParameterObject Long loanId
    ) {
        return ResponseEntity.ok(
                loanOfficerService.approveLoanApplication(loanId)
        );
    }

    @PostMapping("reject")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "To reject a loan application")
    public ResponseEntity<LoanStatus> rejectLoanApplication(
            @ParameterObject Long loanId
    ) {
        return ResponseEntity.ok(
                loanOfficerService.rejectLoanApplication(loanId)
        );
    }

    @PostMapping("agreement")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "To generate a loan agreement")
    public ResponseEntity<LoanAgreement> generateAgreement(
            @RequestBody @Valid AgreementRequest request
    ) {
        return ResponseEntity.ok(
                loanOfficerService.generateAgreement(request)
        );
    }

    @GetMapping("agreements")
    @PreAuthorize("hasAuthority('LOAN_OFFICER')")
    @Operation(summary = "all loan agreements generated by a loan officer")
    public ResponseEntity<Page<LoanAgreement>> allLoanAgreementsByAnOfficer(
            @ParameterObject int pageNumber
    ) {
        return ResponseEntity.ok(
                loanOfficerService.allLoanAgreementByOfficerId(pageNumber)
        );
    }
}