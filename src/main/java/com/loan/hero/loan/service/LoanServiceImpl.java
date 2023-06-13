package com.loan.hero.loan.service;

import com.loan.hero.loan.data.models.Loan;

import com.loan.hero.loan.data.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    @Override
    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }
}
