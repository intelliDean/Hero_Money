package com.loan.hero.auth.user.service;

import com.loan.hero.auth.user.data.models.HeroToken;
import com.loan.hero.auth.user.data.repositories.HeroTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
        HeroToken classToken = getValidTokenByAnyToken(accessToken)
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
}
