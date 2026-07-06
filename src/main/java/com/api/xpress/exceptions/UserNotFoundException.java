package com.api.xpress.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends XpressException {

    public UserNotFoundException() {
        this("User not found");
    }

    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}
