package com.loan.hero.loan.data.repository;

import com.loan.hero.loan.data.models.Loan;
import org.springframework.data.jpa.repository.JpaRepository;
public interface LoanRepository extends JpaRepository<Loan, Long> {

}
