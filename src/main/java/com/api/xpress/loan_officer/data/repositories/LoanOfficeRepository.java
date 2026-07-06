package com.api.xpress.loan_officer.data.repositories;

import com.api.xpress.loan_officer.data.models.LoanOfficer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LoanOfficeRepository extends JpaRepository<LoanOfficer, Long> {

    Optional<LoanOfficer> findLoanOfficerByUserEmail(String email);
}
