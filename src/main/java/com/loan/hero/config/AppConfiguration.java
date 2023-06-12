package com.loan.hero.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

import javax.crypto.spec.SecretKeySpec;
import java.security.Key;

@Configuration
public class AppConfiguration {
    @Value("${Jwt_Secret_Key}")
    private String jwtSecret;


    @Bean
    public Key getSecretKey() {
        return new SecretKeySpec(
                jwtSecret.getBytes(),
                SignatureAlgorithm.HS512.getJcaName());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public WebClient webClient () {
        return WebClient.builder().build();
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
