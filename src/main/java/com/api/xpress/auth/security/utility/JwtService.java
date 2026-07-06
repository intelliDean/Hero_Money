package com.api.xpress.auth.security.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${token.expiration.access}")
    private long accessExpiration;

    @Value("${token.expiration.refresh}")
    private long refreshExpiration;

    @Value("${app.name}")
    private String issuer;

    private final SecretKey secretKey;


    public String extractUsernameFromToken(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        return claimsResolver.apply(extractAllClaims(token));
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String generateRefreshToken(String email) {
        return generateToken(new HashMap<>(), email, refreshExpiration);
    }

    public String generateAccessToken(Map<String, Object> claims, String email) {
        return generateToken(claims, email, accessExpiration);
    }

    private String generateToken(Map<String, Object> claims, String email, Long expiration) {

        return Jwts.builder()
                .issuer(issuer)
                .issuedAt(Date.from(Instant.now()))
                .claims(claims)
                .subject(email)
                .expiration(Date.from(Instant.now().plus(Duration.ofHours(expiration))))
                .signWith(secretKey)
                .compact();
    }


    public Boolean isValid(String token) {
        try {
            Claims claims = extractAllClaims(token);
            return claims.getExpiration() != null;
        } catch (JwtException e) {
            return false;
        }
    }
}
