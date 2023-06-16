package com.loan.hero.loan_officer.data.repositories;

import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LoanOfficeRepository extends JpaRepository<LoanOfficer, Long> {
    Optional<LoanOfficer> findByUserEmail(String userEmail);
    Optional<LoanOfficer> findByUser(User user);
}
