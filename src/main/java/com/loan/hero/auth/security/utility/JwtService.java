package com.loan.hero.auth.security.utility;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtService {
    @Value("${access_expiration}")
    private long accessExpiration;
    @Value("${refresh_expiration}")
    private long refreshExpiration;
    private final Key key;

    @Autowired
    public JwtService(Key key) {
        this.key = key;
    }

    public String extractUsernameFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String generateRefreshToken(String email) {
        return generateToken(new HashMap<>(), email, refreshExpiration);
    }

    public String generateAccessToken(Map<String, Object> claims, String email) {
        return generateToken(claims, email, accessExpiration);
    }

    private String generateToken(Map<String, Object> claims, String email, Long expiration) {
        final Date expiredAt = Date.from(Instant.now().plusSeconds(expiration));
        return Jwts.builder()
                .setIssuer("Hero")
                .setIssuedAt(Date.from(Instant.now()))
                .setClaims(claims)
                .setSubject(email)
                .setExpiration(expiredAt)
                .signWith(SignatureAlgorithm.HS512, key)
                .compact();
    }

    public Boolean isValid(String token) {
        try {
           final Claims claims = Jwts.parser()
                    .setSigningKey(key)
                    .parseClaimsJws(token)
                    .getBody();
           final Date expiration = claims.getExpiration();
            return expiration != null &&
                    expiration.after(Date.from(Instant.now()));
        } catch (JwtException e) {
            return false;
        }
    }
}
