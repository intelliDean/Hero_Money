package com.loan.hero.auth.user.data.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true,  nullable = false)
    private String email;

    private String password;

    @OneToOne
    private Address address;

    private String phoneNumber;

    private Set<Role> roles;

    private final LocalDateTime registeredAt = LocalDateTime.now();

    private boolean enabled;

}
