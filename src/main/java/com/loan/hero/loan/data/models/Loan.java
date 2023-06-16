package com.loan.hero.loan.data.models;

import com.loan.hero.customer.data.models.Customer;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan")
public class Loan {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    private Customer customer;

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
    private LoanDocuments loanDocuments;
}
