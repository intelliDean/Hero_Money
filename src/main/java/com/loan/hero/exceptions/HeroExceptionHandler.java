package com.loan.hero.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HeroExceptionHandler {

    @ExceptionHandler(HeroException.class)
    public ResponseEntity<HeroExceptionResponse> handleException(
            HeroException exception
    ) {
        return ResponseEntity.badRequest()
                .body(
                        HeroExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(exception.getStatus())
                                .build()
                );
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<HeroExceptionResponse> handleException(
            UserNotAuthorizedException exception
    ) {
        return ResponseEntity.badRequest()
                .body(
                        HeroExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(exception.getStatus())
                                .build()
                );
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<HeroExceptionResponse> handleException(
            UserNotFoundException exception
    ) {
        return ResponseEntity.badRequest()
                .body(
                        HeroExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(exception.getStatus())
                                .build()
                );
    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<HeroExceptionResponse> handleException(
            BadCredentialsException exception
    ) {
        return ResponseEntity.badRequest()
                .body(
                        HeroExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
    }
}
