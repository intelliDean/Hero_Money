package com.loan.hero.agreement.data.model;

import com.loan.hero.loan.data.models.Loan;
import com.loan.hero.loan_officer.data.models.LoanOfficer;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_agreement")
public class LoanAgreement {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private Loan loan;

    @ManyToOne(cascade = CascadeType.ALL)
    private LoanOfficer loanOfficer;

    private LocalDateTime generatedAt;

    private boolean agreed;
}

