package com.api.xpress.auth.user.data.dtos.request;

import com.api.xpress.auth.user.data.models.User;
import lombok.*;

@Builder
public record XpressTokenRequest(

        String accessToken,
        String refreshToken,
        User user
) {
}
