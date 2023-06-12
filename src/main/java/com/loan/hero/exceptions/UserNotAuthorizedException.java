package com.loan.hero.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends HeroException {

    public UserNotAuthorizedException() {
        this("Unauthorized");
    }

    public UserNotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
