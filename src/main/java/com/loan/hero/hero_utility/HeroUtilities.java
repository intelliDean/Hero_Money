package com.loan.hero.hero_utility;

import java.security.SecureRandom;
import java.util.Base64;

public class HeroUtilities {
    public static final String BEARER = "Bearer ";
    public static String generateToken(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
