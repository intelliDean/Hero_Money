package com.loan.hero.customer.data.models;

import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.customer.data.models.enums.Gender;
import com.loan.hero.customer.data.models.enums.JobStatus;
import com.loan.hero.customer.data.models.enums.MaritalStatus;
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

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    @Enumerated(EnumType.STRING)
    private JobStatus jobStatus;

    private BigDecimal salary;

    private String companyName;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    private String formOfIdentity;

    private boolean complete;
}
