package com.api.xpress.auth.security.utility;

import lombok.*;

@Builder
public record AuthenticationToken(
        String accessToken,
        String refreshToken
) {
}
