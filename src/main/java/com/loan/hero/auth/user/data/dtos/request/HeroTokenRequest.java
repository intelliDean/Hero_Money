package com.loan.hero.auth.user.data.dtos.request;

import com.loan.hero.auth.user.data.models.User;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HeroTokenRequest {

    private String accessToken;
    private String refreshToken;
    private User user;
}
