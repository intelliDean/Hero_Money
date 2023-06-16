package com.loan.hero.customer.data.dto.response;

import com.loan.hero.auth.user.data.dtos.UserDTO;
import com.loan.hero.customer.data.models.Gender;
import com.loan.hero.customer.data.models.JobStatus;
import com.loan.hero.customer.data.models.MaritalStatus;
import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {

    private UserDTO user;

    private int age;

    private MaritalStatus maritalStatus;

    private JobStatus jobStatus;

    private BigDecimal salary;

    private String companyName;

    private Gender gender;

    private String paySlip;

    private String formOfIdentity;

    private String accountStatement;
}
