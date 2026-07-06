package com.api.xpress.auth.user.service;

import com.api.xpress.auth.user.data.mappers.UserMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.api.xpress.auth.security.user.AuthenticatedUser;
import com.api.xpress.auth.security.utility.AuthenticationToken;
import com.api.xpress.auth.security.utility.JwtService;
import com.api.xpress.auth.user.data.dtos.UserDTO;
import com.api.xpress.auth.user.data.models.XpressToken;
import com.api.xpress.auth.user.data.models.User;
import com.api.xpress.auth.user.data.repositories.UserRepository;
import com.api.xpress.exceptions.XpressException;
import com.api.xpress.exceptions.UserNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.api.xpress.xpress_utils.XpressUtils.BEARER;
import static org.apache.http.HttpHeaders.AUTHORIZATION;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final XpressTokenService xpressTokenService;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

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
                xpressTokenService.revokeToken(accessToken);
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
    public User getCurrentUser() { // if i have to get current user at all and I can't pass it from the controller
        try {
            final AuthenticatedUser authenticatedUser =
                    (AuthenticatedUser) Objects.requireNonNull(SecurityContextHolder
                                    .getContext()
                                    .getAuthentication())
                            .getPrincipal();
            assert authenticatedUser != null;
            return authenticatedUser.user();
        } catch (Exception ex) {
            throw new UserNotFoundException();
        }
    }

    @Override
    public UserDTO currentUser(AuthenticatedUser currentUser) {
        return userMapper.toDTO(currentUser.user());
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
                final XpressToken xpressToken = xpressTokenService.getValidTokenByAnyToken(refreshToken)
                        .orElseThrow(() -> new XpressException("Token could not be found"));
                xpressToken.setAccessToken(accessToken);
                xpressTokenService.saveToken(xpressToken);

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
