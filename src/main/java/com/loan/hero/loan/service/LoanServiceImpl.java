package com.loan.hero.loan.service;

import com.loan.hero.exceptions.HeroException;
import com.loan.hero.exceptions.UserNotFoundException;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan.data.repository.LoanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    @Override
    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Loan findById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(HeroException::new);
    }

    @Override
    public List<Loan> allLoansByCustomerId(Long customerId) {
        return loanRepository.findAllByCustomerId(customerId);
    }

    @Override
    public Loan approvedApplication(Long loanId) {
        return loanRepository.findApprovedApplicationById(loanId)
                .orElseThrow(()-> new UserNotFoundException("Approved application not found"));
    }

    @Override
    public List<Loan> allFreshApplication() {
        return loanRepository.findAllFreshLoanApplication();
    }
}