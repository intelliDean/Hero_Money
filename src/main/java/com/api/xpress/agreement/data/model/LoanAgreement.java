package com.api.xpress.agreement.data.model;

import com.api.xpress.loan.data.models.Loan;
import com.api.xpress.loan_officer.data.models.LoanOfficer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_agreement")
@EntityListeners(AuditingEntityListener.class)
public class LoanAgreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_id")
    private Loan loan;

    @JoinColumn(name = "loan_officer_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private LoanOfficer loanOfficer;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime generatedAt;


    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean agreed = false;
}

