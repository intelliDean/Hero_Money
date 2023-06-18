package com.loan.hero.agreement.data.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
@DataJpaTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
class LoanAgreementRepositoryTest {

    @Autowired
    private LoanAgreementRepository loanAgreementRepository;

    @Test
    void findAllLoanAgreementsByLoanOfficerId() {

    }
}