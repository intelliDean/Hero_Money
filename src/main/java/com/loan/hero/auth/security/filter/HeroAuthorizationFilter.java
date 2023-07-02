package com.loan.hero.auth.security.filter;

import com.loan.hero.auth.security.utility.JwtService;
import com.loan.hero.auth.user.service.HeroTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static com.loan.hero.hero_utility.HeroUtilities.BEARER;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Slf4j
@AllArgsConstructor
public class HeroAuthorizationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final HeroTokenService heroTokenService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (StringUtils.hasText(authHeader) &&
                StringUtils.startsWithIgnoreCase(authHeader, BEARER)) {
            final String accessToken = authHeader.substring(BEARER.length());
            if (jwtService.isValid(accessToken) &&
                    heroTokenService.isTokenValid(accessToken)) {
                final String email = jwtService.extractUsernameFromToken(accessToken);
                if (email != null) {
                    UserDetails userDetails =
                            userDetailsService.loadUserByUsername(email);
                    final UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(
                                            request
                                    )
                    );
                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(
                                    authenticationToken
                            );
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
