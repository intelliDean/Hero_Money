package com.api.xpress.customer.data.models;

import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.customer.data.models.enums.Gender;
import com.api.xpress.customer.data.models.enums.JobStatus;
import com.api.xpress.customer.data.models.enums.MaritalStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "customers")
@EntityListeners(AuditingEntityListener.class)
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", nullable = false)
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
