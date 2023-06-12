package com.loan.hero.auth.user.service;


import com.loan.hero.auth.user.data.models.HeroToken;

import java.util.Optional;

public interface HeroTokenService {
    void saveToken(HeroToken heroToken);
    Optional<HeroToken> getValidTokenByAnyToken(String anyToken);
    void revokeToken(String accessToken);
    boolean isTokenValid(String anyToken);
}
