package com.crm.food.establishment.user.auth.controller;

import com.crm.food.establishment.user.ApiErrorInfo;

import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.exception.InvalidUserCredentialsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = {
            InvalidTokenException.class
    })
    public ResponseEntity<ApiErrorInfo> handleInvalidToken(
            InvalidTokenException exception
    ) {
        ApiErrorInfo errorInfo = mapApiErrorInfo(
                InvalidTokenException.readableName(),
                exception
        );

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {
            InvalidUserCredentialsException.class
    })
    public ResponseEntity<ApiErrorInfo> handleInvalidUserCredentials(
            InvalidUserCredentialsException exception
    ) {
        ApiErrorInfo errorInfo = mapApiErrorInfo(
                InvalidUserCredentialsException.readableName(),
                exception
        );

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    private ApiErrorInfo mapApiErrorInfo(
            String title,
            RuntimeException exception
    ) {
        return ApiErrorInfo.builder()
                .title(title)
                .status(HttpStatus.UNAUTHORIZED)
                .description(exception.getMessage())
                .build();
    }
}
