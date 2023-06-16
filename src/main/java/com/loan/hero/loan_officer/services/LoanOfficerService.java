package com.loan.hero.loan_officer.services;

import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan.data.models.LoanStatus;
import com.loan.hero.loan_officer.data.dto.request.AgreementRequest;
import com.loan.hero.loan_officer.data.dto.request.InviteRequest;
import com.loan.hero.loan_officer.data.dto.request.OfficerRequest;
import com.loan.hero.loan_officer.data.dto.request.UpdateLoanRequest;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import org.springframework.data.domain.Page;

public interface LoanOfficerService {

    String inviteAdmin (InviteRequest request);
    LoanOfficer cuurentLoanOfficer ();
    AuthenticationToken completeOfficerProfile (OfficerRequest officerRequest);
    LoanOfficer findByUserEmail(String email);
    LoanStatus updateLoanStatus(UpdateLoanRequest request);
    LoanStatus approveLoanApplication(Long loanId);
    LoanStatus rejectLoanApplication(Long loanId);
    LoanAgreement generateAgreement(AgreementRequest request);
    Page<Loan> allFreshLoans(int pageNumber);
    Page<LoanAgreement> allLoanAgreementByOfficerId(int pageNumber);
}