package com.api.xpress.auth.security.filter;

import com.api.xpress.auth.user.data.dtos.request.LoginRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.auth.security.utility.JwtService;
import com.api.xpress.auth.user.data.models.XpressToken;
import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.auth.user.service.XpressTokenService;
import com.api.xpress.exceptions.XpressException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@AllArgsConstructor
public class XpressAuthenticateFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final XpressTokenService xpressTokenService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    @Override
    public @Nullable Authentication attemptAuthentication(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response
    ) throws AuthenticationException {

        try {
            final LoginRequest user = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            final Authentication authentication = new UsernamePasswordAuthenticationToken(
                    user.email(),
                    user.password()
            );
            final Authentication authenticationResult = authenticationManager.authenticate(authentication);

            SecurityContextHolder.getContext().setAuthentication(authenticationResult);
            return SecurityContextHolder.getContext().getAuthentication();
        } catch (IOException e) {
            throw new XpressException("Authentication failed");
        }
    }

    @Override
    protected void successfulAuthentication(
            @NonNull HttpServletRequest request,
            HttpServletResponse response,
            @NonNull FilterChain chain,
            Authentication authResult) throws IOException {

        final Map<String, Object> claims = new HashMap<>();

        List<String> roles = authResult.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();
        claims.put("roles", roles);

        final String email = Objects.requireNonNull(authResult.getPrincipal()).toString();

        final String accessToken = jwtService.generateAccessToken(claims, email);
        final String refreshToken = jwtService.generateRefreshToken(email);

        final AuthenticatedUser authenticatedUser = (AuthenticatedUser) userDetailsService.loadUserByUsername(email);

        final XpressToken xpressToken = XpressToken.builder()
                .user(authenticatedUser.user())
                .refreshToken(refreshToken)
                .accessToken(accessToken)
                .build();
        xpressTokenService.saveToken(xpressToken);

        final AuthenticationToken authenticationToken =
                AuthenticationToken.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .build();

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getOutputStream(), authenticationToken);
    }
}
