package com.api.xpress.loan.service;

import com.api.xpress.exceptions.XpressException;
import com.api.xpress.exceptions.UserNotFoundException;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan.data.repository.LoanRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanServiceImpl implements LoanService {
    private final LoanRepository loanRepository;

    @Override
    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    @Override
    public Loan findById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(XpressException::new);
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