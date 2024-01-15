package com.crm.food.establishment.user.auth.controller;

import com.crm.food.establishment.user.ApiErrorDTO;
import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.exception.InvalidUserCredentialsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidToken(InvalidTokenException exception) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidTokenException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidUserCredentialsException.class)
    public ResponseEntity<ApiErrorDTO> handleInvalidUserCredentials(InvalidUserCredentialsException exception) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidUserCredentialsException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }
}
