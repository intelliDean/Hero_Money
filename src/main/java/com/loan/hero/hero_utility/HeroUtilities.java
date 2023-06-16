package com.loan.hero.hero_utility;

import java.security.SecureRandom;
import java.util.Base64;

public class HeroUtilities {
    public static final String BEARER = "Bearer ";
    public static final String NOT_NULL = "Cannot be null";
    public static final String NOT_BLANK = "Cannot be blank";
    public static final int MAX_NUMBER_PER_PAGE = 5;

    public static String generateToken(int length) {
        byte[] bytes = new byte[length];
        new SecureRandom().nextBytes(bytes);
        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(bytes);
    }
}
