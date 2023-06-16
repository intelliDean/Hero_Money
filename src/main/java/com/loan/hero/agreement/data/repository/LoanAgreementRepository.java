package com.loan.hero.agreement.data.repository;

import com.loan.hero.agreement.data.model.LoanAgreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanAgreementRepository extends JpaRepository<LoanAgreement, Long> {
    List<LoanAgreement> findAllByLoanOfficerId(Long loanOfficerId);
}