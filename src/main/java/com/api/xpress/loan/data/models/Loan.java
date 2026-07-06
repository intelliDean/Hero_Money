package com.api.xpress.loan.data.models;

import com.api.xpress.customer.data.models.Customer;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loans")
@EntityListeners(AuditingEntityListener.class)
public class Loan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime applicationDate;

    private String loanPurpose;

    private BigDecimal loanAmount;

    private int repaymentTerm;

    private BigDecimal interestRate;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    private LocalDateTime disbursementDate;

    @Enumerated(EnumType.STRING)
    private PaymentFrequency paymentFrequency;

    private BigDecimal repaymentAmount;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "loan_documents_id")
    private LoanDocuments loanDocuments;
}
