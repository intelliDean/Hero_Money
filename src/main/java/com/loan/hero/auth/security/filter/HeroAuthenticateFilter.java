package com.loan.hero.auth.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.hero.auth.security.user.AuthenticatedUser;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.auth.security.utility.JwtService;
import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.auth.user.service.HeroTokenService;
import com.loan.hero.exceptions.HeroException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@AllArgsConstructor
public class HeroAuthenticateFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final HeroTokenService heroTokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest request,
            HttpServletResponse response) throws AuthenticationException {
        try {
            final User user = objectMapper.readValue(request.getInputStream(), User.class);

            final Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    );
            final Authentication authenticationResult =
                    authenticationManager.authenticate(authentication);
            if (authenticationResult != null) {
                SecurityContextHolder.getContext().setAuthentication(authenticationResult);
                return SecurityContextHolder.getContext().getAuthentication();
            }
        } catch (IOException e) {
            throw new HeroException("Authentication failed");
        }
        throw new HeroException("Authentication failed");
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain,
            Authentication authResult) throws IOException {

        final Map<String, Object> claims = new HashMap<>();
        authResult.getAuthorities().forEach(
                role -> claims.put("claim", role)
        );
        final String email = authResult.getPrincipal().toString();

        final String accessToken = jwtService.generateAccessToken(claims, email);
        final String refreshToken = jwtService.generateRefreshToken(email);

       final AuthenticatedUser authenticatedUser =
                (AuthenticatedUser) userDetailsService.loadUserByUsername(email);

        final HeroToken heroToken = HeroToken.builder()
                .user(authenticatedUser.getUser())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .revoked(false)
                .build();
        heroTokenService.saveToken(heroToken);

        final AuthenticationToken authenticationToken =
                AuthenticationToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), authenticationToken);
    }
}
