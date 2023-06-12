package com.loan.hero.auth.user.data.dtos;

import com.loan.hero.auth.user.data.models.Role;

import java.time.LocalDateTime;
import java.util.Set;

public class UserDTO {
    private String firstName;

    private String lastName;

    private String email;

    private Set<Role> roles;

    private final LocalDateTime registeredAt = LocalDateTime.now();

    private boolean enabled;
}
