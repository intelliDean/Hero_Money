package com.api.xpress.auth.user.service;


import com.api.xpress.auth.user.data.models.XpressToken;

import java.util.Optional;

public interface XpressTokenService {
    void saveToken(XpressToken xpressToken);
    Optional<XpressToken> getValidTokenByAnyToken(String anyToken);
    void revokeToken(String accessToken);
    boolean isTokenValid(String anyToken);
}
