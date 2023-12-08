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
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .code(InvalidTokenException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {
            InvalidUserCredentialsException.class
    })
    public ResponseEntity<ApiErrorInfo> handleInvalidUserCredentials(
            InvalidUserCredentialsException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .code(InvalidUserCredentialsException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.UNAUTHORIZED);
    }
}
