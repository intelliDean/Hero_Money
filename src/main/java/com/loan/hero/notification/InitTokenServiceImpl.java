package com.loan.hero.notification;

import com.loan.hero.notification.interfaces.InitTokenService;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Scheduled(cron = "0 0 0 * * ?", zone = "Africa/Lagos")
    private void deleteAllRevokedTokens() {
        final List<InitToken> allRevokedTokens =
                initTokenRepository.findAllRevokedTokens();
        if (!allRevokedTokens.isEmpty()) {
            initTokenRepository.deleteAll(allRevokedTokens);
        }
    }

    @Scheduled(cron = "0 0 * * * ?", zone = "Africa/Lagos")
    private void setExpiredToken() {
        final List<InitToken> tokens = initTokenRepository.findAllValidTokens();
        tokens.stream().filter(
                token -> token.getExpireAt()
                        .isBefore(LocalDateTime.now())
        ).forEach(init -> init.setExpired(true));
        initTokenRepository.saveAll(tokens);
    }
}

