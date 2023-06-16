package com.loan.hero.auth.user.data.dtos;

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
public class UserDTO {

    private String firstName;

    private String lastName;

    private String email;

    @JsonUnwrapped
    private Address address;

    private Set<Role> roles;

    private LocalDateTime registeredAt;

    private boolean enabled;
}
