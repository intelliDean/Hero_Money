package com.loan.hero.auth.user.service;

import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.repositories.HeroTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class HeroTokenServiceImpl implements HeroTokenService {
    private final HeroTokenRepository heroTokenRepository;

    @Override
    public void saveToken(HeroToken heroToken) {
        heroTokenRepository.save(heroToken);
    }

    @Override
    public Optional<HeroToken> getValidTokenByAnyToken(String anyToken) {
        return heroTokenRepository.findValidTokenByToken(anyToken);
    }

    @Override
    public void revokeToken(String accessToken) {
        final HeroToken classToken = getValidTokenByAnyToken(accessToken)
                .orElse(null);
        if (classToken != null) {
            classToken.setRevoked(true);
            heroTokenRepository.save(classToken);
        }
    }

    @Override
    public boolean isTokenValid(String anyToken) {
        return getValidTokenByAnyToken(anyToken)
                .map(heroToken -> !heroToken.isRevoked())
                .orElse(false);
    }

    @Scheduled(cron = "0 0 0 * * ?", zone = "Africa/Lagos")
    private void deleteAllRevokedTokens() {
        final List<HeroToken> allRevokedTokens =
                heroTokenRepository.findAllInvalidTokens();
        if (!allRevokedTokens.isEmpty()) {
            heroTokenRepository.deleteAll(allRevokedTokens);
        }
    }

    @Scheduled(cron = "0 0 */6 * * *", zone = "Africa/Lagos")
    private void setTokenExpiration() {
       final List<HeroToken> notExpiredTokens =
                heroTokenRepository.findAllTokenNotExpired();
        notExpiredTokens.stream()
                .filter(
                        token -> token.getCreatedAt()
                                .plusDays(7)
                                .isBefore(LocalDateTime.now())
                        )
                .forEach(token -> token.setExpired(true));
            heroTokenRepository.saveAll(notExpiredTokens);
    }
}
