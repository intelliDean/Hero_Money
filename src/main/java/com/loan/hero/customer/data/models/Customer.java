package com.loan.hero.customer.data.models;

import com.loan.hero.auth.user.data.models.JobStatus;
import com.loan.hero.auth.user.data.models.MaritalStatus;
import com.loan.hero.auth.user.data.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    private int age;

    private MaritalStatus maritalStatus;

    private JobStatus jobStatus;

    private BigDecimal salary;

    private String companyName;

    private String paySlip;

    private String formOfIdentity;

    private String accountStatement;

}
