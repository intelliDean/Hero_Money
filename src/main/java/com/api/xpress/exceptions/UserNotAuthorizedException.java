package com.api.xpress.exceptions;

import org.springframework.http.HttpStatus;

public class UserNotAuthorizedException extends XpressException {

    public UserNotAuthorizedException() {
        this("Unauthorized");
    }

    public UserNotAuthorizedException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}
