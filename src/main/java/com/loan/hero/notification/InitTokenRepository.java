package com.loan.hero.notification;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface InitTokenRepository extends JpaRepository<InitToken, Long> {
    @Query("""
            select token from InitToken token
            where token.email = :email and token.token = :token and token.revoked = false
            """)
    Optional<InitToken> findValidByTokenAndEmail(String token, String email);

    @Query("""
            select tokens from InitToken tokens
            where tokens.revoked = true or tokens.expired = true
            """)
    List<InitToken> findAllRevokedTokens();

    @Query("""
              select token from InitToken token
            where token.revoked = false and token.expired = false
            """)
    List<InitToken> findAllValidTokens();
}
