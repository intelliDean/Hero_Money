package com.api.xpress.agreement.service;

import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan_officer.data.models.LoanOfficer;

import java.util.List;


public interface LoanAgreementService {
    LoanAgreement saveAgreement (Loan loan, LoanOfficer loanOfficer);
    LoanAgreement findById(Long loanAgreementId);

    void save(LoanAgreement loanAgreement);

    List<LoanAgreement> allAgreementsByLoanOfficer(Long loanOfficerId);


}
