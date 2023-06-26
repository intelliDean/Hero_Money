package com.loan.hero.notification;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class InitToken {
    @Id
    @GeneratedValue
    private Long id;

    private String token;

    private String email;
    private boolean revoked;

    private final LocalDateTime generatedAt = LocalDateTime.now();

    private final LocalDateTime expireAt = generatedAt.plusHours(3);

    private boolean expired;
            //= expireAt.isBefore(LocalDateTime.now());
}
