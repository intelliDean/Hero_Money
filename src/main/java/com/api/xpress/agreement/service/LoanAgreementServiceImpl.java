package com.api.xpress.agreement.service;

import com.api.xpress.agreement.data.model.LoanAgreement;
import com.api.xpress.agreement.data.repository.LoanAgreementRepository;
import com.api.xpress.exceptions.XpressException;
import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan_officer.data.models.LoanOfficer;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class LoanAgreementServiceImpl implements LoanAgreementService {
    private final LoanAgreementRepository loanAgreementRepository;


    @Override
    public LoanAgreement saveAgreement(Loan loan, LoanOfficer loanOfficer) {
        final LoanAgreement loanAgreement = LoanAgreement.builder()
                .loan(loan)
                .loanOfficer(loanOfficer)
                .build();
        return loanAgreementRepository.save(loanAgreement);
    }

    @Override
    public LoanAgreement findById(Long loanAgreementId) {
        return loanAgreementRepository.findById(loanAgreementId)
                .orElseThrow(()-> new XpressException("Loan Agreement not found"));
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