package com.loan.hero.loan.data.repository;

import com.loan.hero.loan.data.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LoanRepository extends JpaRepository<Loan, Long> {

    List<Loan> findAllByCustomerId(Long customerId);
    @Query("""
            select loan from Loan loan
            where loan.loanStatus = "PENDING"
            """)
    List<Loan> findAllFreshLoanApplication();
    @Query("""
            select loan from Loan loan
            where loan.id  = :loanId and loan.loanStatus = "APPROVED"
            """)
    Optional<Loan> findApprovedApplicationById(Long loanId);
}
