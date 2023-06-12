package com.loan.hero.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class HeroExceptionResponse {

    private Map<String, String> data;

    private String message;

    private HttpStatus status;
}
