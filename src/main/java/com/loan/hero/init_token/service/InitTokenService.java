package com.loan.hero.init_token.service;


import com.loan.hero.init_token.InitToken;

import java.util.Optional;

public interface InitTokenService {

    void saveToken(InitToken initToken);
    boolean isValid(InitToken initToken);
    Optional<InitToken> findByTokenAndEmail(String token, String email);
}
