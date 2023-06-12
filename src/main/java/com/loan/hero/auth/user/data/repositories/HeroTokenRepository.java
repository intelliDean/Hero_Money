package com.loan.hero.auth.user.data.repositories;

import com.loan.hero.auth.user.data.models.HeroToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface HeroTokenRepository extends JpaRepository<HeroToken, Long> {
    @Query("""
            select token from HeroToken token
            where token.accessToken = :anyToken or token.refreshToken = :anyToken and token.revoked = false
            """)
    Optional<HeroToken> findValidTokenByToken(String anyToken);
}
