package com.loan.hero.auth.user.data.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroToken {
    @Id
    @GeneratedValue
    private Long id;

    private String accessToken;

    private String refreshToken;

    private boolean revoked;

    private boolean expired;

    private final LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    private User user;
}
