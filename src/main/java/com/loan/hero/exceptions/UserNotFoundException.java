package com.loan.hero.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends HeroException {

    public UserNotFoundException() {
        this("User not found");
    }

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
