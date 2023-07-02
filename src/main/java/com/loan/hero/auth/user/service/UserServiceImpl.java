package com.loan.hero.auth.user.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.loan.hero.auth.security.user.AuthenticatedUser;
import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.auth.security.utility.JwtService;
import com.loan.hero.auth.user.data.dtos.UserDTO;
import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.auth.user.data.repositories.UserRepository;
import com.loan.hero.exceptions.HeroException;
import com.loan.hero.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.loan.hero.hero_utility.HeroUtilities.BEARER;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final HeroTokenService heroTokenService;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void logout(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (StringUtils.hasText(header) &&
                StringUtils.startsWithIgnoreCase(header, BEARER)) {
            final String accessToken = header.substring(BEARER.length());
            if (jwtService.isValid(accessToken)) {
                heroTokenService.revokeToken(accessToken);
                SecurityContextHolder.clearContext();

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(
                        response.getOutputStream(),
                        "User Logout successfully"
                );
            }
        }
    }

    @Override
    public User getCurrentUser() {
        try {
            final AuthenticatedUser authenticatedUser =
                    (AuthenticatedUser) SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            .getPrincipal();
            return authenticatedUser.getUser();
        } catch (Exception ex) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserDTO currentUser() {
        return new ModelMapper().map(
                getCurrentUser(),
                UserDTO.class
        );
    }

    @Override
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        final String authHeader = request.getHeader(AUTHORIZATION);
        if (!StringUtils.hasText(authHeader) ||
                !StringUtils.startsWithIgnoreCase(authHeader, BEARER)) return;
        final String refreshToken = authHeader.substring(BEARER.length());

        if (jwtService.isValid(refreshToken)) {
            final String email = jwtService.extractUsernameFromToken(refreshToken);

            if (StringUtils.hasText(email)) {
                final User user = findUserByEmail(email);

                final String accessToken = jwtService.generateAccessToken(
                        getUserAuthority(user),
                        user.getEmail()
                );
                final AuthenticationToken newLoginTokens =
                        AuthenticationToken.builder()
                                .accessToken(accessToken)
                                .refreshToken(refreshToken)
                                .build();
                final HeroToken heroToken = heroTokenService.getValidTokenByAnyToken(refreshToken)
                        .orElseThrow(() -> new HeroException("Token could not be found"));
                heroToken.setAccessToken(accessToken);
                heroTokenService.saveToken(heroToken);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper()
                        .writeValue(
                                response.getOutputStream(),
                                newLoginTokens
                        );
            }
        }
    }
    private static Map<String, Object> getUserAuthority(User savedUser) {
        return savedUser.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(
                        Collectors.toMap(
                                authority -> "claim",
                                Function.identity()
                        )
                );
    }
}
