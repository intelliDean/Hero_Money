package com.loan.hero.loan_officer.data.dto.response;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import com.loan.hero.auth.user.data.models.Address;
import com.loan.hero.auth.user.data.models.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanOfficerDTO {

    private String firstName;

    private String lastName;

    private String email;

    @JsonUnwrapped
    private Address address;

    private String userImage;

    private String phoneNumber;

    private Set<Role> roles;

    private final LocalDateTime registeredAt = LocalDateTime.now();

    private boolean enabled;
}
