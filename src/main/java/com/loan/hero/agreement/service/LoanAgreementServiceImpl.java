package com.loan.hero.agreement.service;

import com.loan.hero.agreement.data.model.LoanAgreement;
import com.loan.hero.agreement.data.repository.LoanAgreementRepository;
import com.loan.hero.exceptions.HeroException;
import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class LoanAgreementServiceImpl implements LoanAgreementService {
    private final LoanAgreementRepository loanAgreementRepository;


    @Override
    public LoanAgreement saveAgreement(Loan loan, LoanOfficer loanOfficer) {
        final LoanAgreement loanAgreement = LoanAgreement.builder()
                .loan(loan)
                .loanOfficer(loanOfficer)
                .generatedAt(LocalDateTime.now())
                .agreed(false)
                .build();
        return loanAgreementRepository.save(loanAgreement);
    }

    @Override
    public LoanAgreement findById(Long loanAgreementId) {
        return loanAgreementRepository.findById(loanAgreementId)
                .orElseThrow(()-> new HeroException("Loan Agreement not found"));
    }

    @Override
    public void save(LoanAgreement loanAgreement) {
        loanAgreementRepository.save(loanAgreement);
    }

    @Override
    public List<LoanAgreement> allAgreementsByLoanOfficer(Long loanOfficerId) {
        return loanAgreementRepository.findAllByLoanOfficerId(loanOfficerId);
    }
}