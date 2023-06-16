package com.loan.hero.notification;

import com.loan.hero.notification.interfaces.InitTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class InitTokenServiceImpl implements InitTokenService {
    private final InitTokenRepository initTokenRepository;

    @Override
    public void saveToken(InitToken initToken) {
        initTokenRepository.save(initToken);
    }

    @Override
    public boolean isValid(InitToken initToken) {
        return initToken != null &&
                initToken.getExpireAt()
                        .isAfter(LocalDateTime.now());
    }

    @Override
    public Optional<InitToken> findByTokenAndEmail(String token, String email) {
        return initTokenRepository.findValidByTokenAndEmail(token, email);
    }
}
