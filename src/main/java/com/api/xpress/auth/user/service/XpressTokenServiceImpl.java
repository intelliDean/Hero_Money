package com.api.xpress.auth.user.service;

import com.api.xpress.auth.user.data.models.XpressToken;
import com.api.xpress.auth.user.data.repositories.XpressTokenRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class XpressTokenServiceImpl implements XpressTokenService {

    private final XpressTokenRepository xpressTokenRepository;

    @Override
    public void saveToken(XpressToken xpressToken) {
        xpressTokenRepository.save(xpressToken);
    }

    @Override
    public Optional<XpressToken> getValidTokenByAnyToken(String anyToken) {
        return xpressTokenRepository.findValidTokenByToken(anyToken);
    }

    @Override
    public void revokeToken(String accessToken) {
        final XpressToken classToken = getValidTokenByAnyToken(accessToken)
                .orElse(null);
        if (classToken != null) {
            classToken.setRevoked(true);
            xpressTokenRepository.save(classToken);
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
        final List<XpressToken> allRevokedTokens =
                xpressTokenRepository.findAllInvalidTokens();
        if (!allRevokedTokens.isEmpty()) {
            xpressTokenRepository.deleteAll(allRevokedTokens);
        }
    }

    @Scheduled(cron = "0 0 */6 * * *", zone = "Africa/Lagos")
    private void setTokenExpiration() {
       final List<XpressToken> notExpiredTokens =
                xpressTokenRepository.findAllTokenNotExpired();
        notExpiredTokens.stream()
                .filter(
                        token -> token.getCreatedAt()
                                .plusDays(7)
                                .isBefore(LocalDateTime.now())
                        )
                .forEach(token -> token.setExpired(true));
            xpressTokenRepository.saveAll(notExpiredTokens);
    }
}
