package com.loan.hero.loan.service;

import com.loan.hero.loan.data.models.Loan;

import java.util.List;

public interface LoanService {

    Loan saveLoan (Loan loan);
    Loan findById(Long loanId);
    List<Loan> allLoansByCustomerId(Long customerId);
    Loan approvedApplication(Long loanId);
    List<Loan> allFreshApplication();
}
