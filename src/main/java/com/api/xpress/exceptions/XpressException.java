package com.api.xpress.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class XpressException extends RuntimeException{

    @Getter
    private final HttpStatus status;

    public XpressException() {
        this("An error occurred");
    }

    public XpressException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public XpressException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
