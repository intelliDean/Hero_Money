package com.loan.hero.notification.interfaces;


import com.loan.hero.notification.InitToken;

import java.util.Optional;

public interface InitTokenService {

    void saveToken(InitToken initToken);
    boolean isValid(InitToken initToken);
    Optional<InitToken> findByTokenAndEmail(String token, String email);
}
