package com.loan.hero.agreement.service;

import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan_officer.data.models.LoanOfficer;

import java.util.List;


public interface LoanAgreementService {
    LoanAgreement saveAgreement (Loan loan, LoanOfficer loanOfficer);
    LoanAgreement findById(Long loanAgreementId);

    void save(LoanAgreement loanAgreement);

    List<LoanAgreement> allAgreementsByLoanOfficer(Long loanOfficerId);


}
