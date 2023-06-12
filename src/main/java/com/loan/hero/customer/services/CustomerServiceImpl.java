package com.loan.hero.customer.services;

import com.loan.hero.auth.security.utility.AuthenticationToken;
import com.loan.hero.auth.security.utility.JwtService;
import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.models.User;
import com.loan.hero.auth.user.service.HeroTokenService;
import com.loan.hero.customer.data.dto.InitRequest;
import com.loan.hero.customer.data.dto.SignUpRequest;
import com.loan.hero.customer.data.dto.response.InitResponse;
import com.loan.hero.customer.data.models.Customer;
import com.loan.hero.customer.data.repositories.CustomerRepository;
import com.loan.hero.exceptions.HeroException;
import com.loan.hero.notification.InitToken;
import com.loan.hero.notification.interfaces.InitTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.loan.hero.auth.user.data.models.Role.COSTUMER;


@Service
@AllArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CustomerRepository customerRepository;
    private final HeroTokenService heroTokenService;
    private final InitTokenService initTokenService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public InitResponse initAccess(InitRequest initRequest) {
        if (customerRepository.existsByUserEmail(initRequest.getEmail())) {
            return InitResponse.LOGIN;
        }
        String emailResponse = initTokenService.sendSignUpMail(initRequest.getEmail());
        if (emailResponse != null)
            return InitResponse.SIGNUP;
        throw new HeroException("Registration failed");
    }

    @Override
    public AuthenticationToken register(SignUpRequest signUpRequest) {
        InitToken initToken = initTokenService.findByTokenAndEmail(
                signUpRequest.getToken(),
                signUpRequest.getEmail()
        ).orElseThrow(HeroException::new);
        if (!initTokenService.isValid(initToken)) {
            throw new HeroException("Invalid credentials");
        }
        User user = User.builder()
                .firstName(signUpRequest.getFirstName())
                .lastName(signUpRequest.getLastName())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .enabled(true)
                .roles(Set.of(COSTUMER))
                .build();

        Customer customer = Customer.builder()
                .user(user)
                .build();
        customerRepository.save(customer);

        initToken.setRevoked(true);
        initTokenService.saveToken(initToken);

        return getAuthenticationToken(user);
    }

    private AuthenticationToken getAuthenticationToken(User user) {
        String email = user.getEmail();
        String accessToken = jwtService.generateAccessToken(
                getUserAuthority(user),
                email
        );
        String refreshToken = jwtService.generateRefreshToken(email);

        HeroToken heroToken = HeroToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .revoked(false)
                .build();
        heroTokenService.saveToken(heroToken);

        return AuthenticationToken.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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
