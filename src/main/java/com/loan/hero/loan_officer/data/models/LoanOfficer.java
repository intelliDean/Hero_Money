package com.loan.hero.loan_officer.data.models;

import com.loan.hero.auth.user.data.models.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "loan_officer")
public class LoanOfficer {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private User user;

    private String employeeId;
}
