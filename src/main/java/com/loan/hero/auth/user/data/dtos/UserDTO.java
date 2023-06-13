package com.loan.hero.auth.user.data.dtos;

import com.loan.hero.auth.user.data.models.Address;
import com.loan.hero.auth.user.data.models.Role;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
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

    private Address address;

    private Set<Role> roles;

    private LocalDateTime registeredAt;

    private boolean enabled;
}
