package com.api.xpress.auth.user.data.repositories;

import com.api.xpress.auth.user.data.models.XpressToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface XpressTokenRepository extends JpaRepository<XpressToken, Long> {
    @Query("""
            select token from XpressToken token
            where token.accessToken = :anyToken or token.refreshToken = :anyToken and token.revoked = false
            """)
    Optional<XpressToken> findValidTokenByToken(String anyToken);

    @Query("""
            select tokens from XpressToken tokens
            where tokens.revoked = true or tokens.expired = true
            """)
    List<XpressToken> findAllInvalidTokens();

    @Query("""
            select token from XpressToken  token
            where token.expired = false or token.revoked = false
            """)
    List<XpressToken> findAllTokenNotExpired();
}
