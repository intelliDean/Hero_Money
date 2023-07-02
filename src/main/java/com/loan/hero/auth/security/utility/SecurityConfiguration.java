package com.loan.hero.auth.security.utility;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.hero.auth.security.filter.HeroAuthenticateFilter;
import com.loan.hero.auth.security.filter.HeroAuthorizationFilter;
import com.loan.hero.auth.user.service.HeroTokenService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
@EnableMethodSecurity(securedEnabled = true)
public class SecurityConfiguration {
    private final AuthenticationEntryPoint authenticationEntryPoint;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final HeroTokenService heroTokenService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(
                        AbstractHttpConfigurer::disable
                )
                .cors(
                        Customizer.withDefaults()
                )
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(
                                authenticationEntryPoint
                        )
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(
                                HttpMethod.POST,
                                WhiteList.freeAccess())
                        .permitAll()
                        .requestMatchers(
                                WhiteList.swagger()
                        ).permitAll()
                        .anyRequest().authenticated())
                .addFilterAt(
                        login(),
                        UsernamePasswordAuthenticationFilter.class
                )
                .addFilterBefore(
                        new HeroAuthorizationFilter(
                                userDetailsService,
                                heroTokenService,
                                jwtService
                        ),
                        HeroAuthenticateFilter.class
                )
                .build();
    }

    private UsernamePasswordAuthenticationFilter login() {
        final UsernamePasswordAuthenticationFilter authenticationFilter =
                new HeroAuthenticateFilter(
                        authenticationManager,
                        heroTokenService,
                        userDetailsService,
                        objectMapper,
                        jwtService
                );
        authenticationFilter.setFilterProcessesUrl("/api/v1/auth/login");
        return authenticationFilter;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Collections.singletonList("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "PATCH"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
