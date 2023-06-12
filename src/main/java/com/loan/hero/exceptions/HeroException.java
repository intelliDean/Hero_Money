package com.loan.hero.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public class HeroException extends RuntimeException{

    @Getter
    private final HttpStatus status;

    public HeroException() {
        this("An error occurred");
    }

    public HeroException(String message) {
        this(message, HttpStatus.BAD_REQUEST);
    }

    public HeroException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }
}
