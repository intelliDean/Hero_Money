package com.loan.hero.auth.user.data.repositories;

import com.loan.hero.auth.user.data.models.HeroToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HeroTokenRepository extends JpaRepository<HeroToken, Long> {
    @Query("""
            select token from HeroToken token
            where token.accessToken = :anyToken or token.refreshToken = :anyToken and token.revoked = false
            """)
    Optional<HeroToken> findValidTokenByToken(String anyToken);

    @Query("""
            select tokens from HeroToken tokens
            where tokens.revoked = true or tokens.expired = true
            """)
    List<HeroToken> findAllInvalidTokens();

    @Query("""
            select token from HeroToken  token
            where token.expired = false or token.revoked = false
            """)
    List<HeroToken> findAllTokenNotExpired();
}
