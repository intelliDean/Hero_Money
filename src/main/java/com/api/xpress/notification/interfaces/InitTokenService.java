package com.api.xpress.notification.interfaces;


import com.api.xpress.notification.InitToken;

import java.util.Optional;

public interface InitTokenService {

    void saveToken(InitToken initToken);
    boolean isValid(InitToken initToken);
    Optional<InitToken> findByTokenAndEmail(String token, String email);
}
