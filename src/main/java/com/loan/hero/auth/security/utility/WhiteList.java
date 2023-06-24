package com.loan.hero.auth.security.utility;

import lombok.Getter;

@Getter
public class WhiteList {
    public static String[] freeAccess() {
        return new String[]{
                "/api/v1/auth/login",
                "/api/v1/auth/signup",
                "/api/v1/customer/init",
                "/api/v1/officer/invite",
                "/api/v1/officer/update",
                "api/v1/customer/register"
        };
    }

    public static String[] swagger() {
        return new String[]{
                "/swagger-ui.html",
                "/swagger-ui/**",
                "/v3/api-docs",
                "/v3/api-docs/**"
        };
    }
}
