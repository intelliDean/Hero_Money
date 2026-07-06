package com.api.xpress.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.stream.Collectors;

@ControllerAdvice
public class XpressExceptionHandler {

    @ExceptionHandler(XpressException.class)
    public ResponseEntity<XpressExceptionResponse> handleException(
            XpressException exception
    ) {
        return ResponseEntity.status(exception.getStatus())
                .body(
                        XpressExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(exception.getStatus())
                                .build()
                );
    }

    @ExceptionHandler(UserNotAuthorizedException.class)
    public ResponseEntity<XpressExceptionResponse> handleException(
            UserNotAuthorizedException exception
    ) {
        return ResponseEntity.status(exception.getStatus())
                .body(
                        XpressExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(exception.getStatus())
                                .build()
                );
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<XpressExceptionResponse> handleException(
            UserNotFoundException exception
    ) {
        return ResponseEntity.status(exception.getStatus())
                .body(
                        XpressExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(exception.getStatus())
                                .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<XpressExceptionResponse> handleException(
            BadCredentialsException exception
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(
                        XpressExceptionResponse.builder()
                                .message(exception.getMessage())
                                .status(HttpStatus.UNAUTHORIZED)
                                .build()
                );
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<XpressExceptionResponse> handleBindException(
            BindException exception
    ) {
        String validationErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        XpressExceptionResponse.builder()
                                .message("Validation failed: " + validationErrors)
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<XpressExceptionResponse> handleMaxUploadSizeExceededException(
            MaxUploadSizeExceededException exception
    ) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(
                        XpressExceptionResponse.builder()
                                .message("File size exceeds the maximum upload limit of 5MB")
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<XpressExceptionResponse> handleGenericException(
            Exception exception
    ) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                        XpressExceptionResponse.builder()
                                .message("An unexpected error occurred. Please try again later.")
                                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .build()
                );
    }
}
