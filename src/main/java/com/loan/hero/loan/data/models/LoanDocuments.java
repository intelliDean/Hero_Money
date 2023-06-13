package com.loan.hero.loan.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_documents")
public class LoanDocuments {
    @Id
    @GeneratedValue
    private Long id;

    private String paySlip;

    private String bankStatement;
}
