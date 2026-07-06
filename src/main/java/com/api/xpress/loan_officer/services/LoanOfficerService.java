package com.api.xpress.loan_officer.services;

import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan.data.models.LoanStatus;
import com.api.xpress.loan_officer.data.dto.request.AgreementRequest;
import com.api.xpress.loan_officer.data.dto.request.InviteRequest;
import com.api.xpress.loan_officer.data.dto.request.OfficerRequest;
import com.api.xpress.loan_officer.data.dto.request.UpdateLoanRequest;
import com.api.xpress.loan_officer.data.models.LoanOfficer;
import org.springframework.data.domain.Page;

public interface LoanOfficerService {

    String inviteAdmin (InviteRequest request);
    LoanOfficer currentLoanOfficer (AuthenticatedUser currentUser);
    AuthenticationToken completeOfficerProfile (OfficerRequest officerRequest);
    LoanOfficer findByUserEmail(String email);
    LoanStatus updateLoanStatus(UpdateLoanRequest request);
    LoanStatus approveLoanApplication(Long loanId);
    LoanStatus rejectLoanApplication(Long loanId);
    LoanAgreement generateAgreement(AgreementRequest request, AuthenticatedUser currentUser);
    Page<Loan> allFreshLoans(int pageNumber);
    Page<LoanAgreement> allLoanAgreementByOfficerId(int pageNumber, AuthenticatedUser currentUser);
}