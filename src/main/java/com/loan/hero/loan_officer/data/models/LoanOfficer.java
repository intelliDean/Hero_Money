package com.loan.hero.loan_officer.data.models;

import com.loan.hero.auth.user.data.models.User;
import jakarta.persistence.*;
import lombok.*;

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

    @OneToOne(cascade = CascadeType.ALL)
    private User user;

    private String employeeId;
}
