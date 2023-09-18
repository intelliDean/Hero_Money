package com.loan.hero.agreement.data.repository;

import com.loan.hero.agreement.data.model.LoanAgreement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface LoanAgreementRepository extends JpaRepository<LoanAgreement, Long> {
    List<LoanAgreement> findAllByLoanOfficerId(Long loanOfficerId);
}