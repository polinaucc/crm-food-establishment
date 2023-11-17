package com.crmfoodestablishment.userauthservice.authservice.controller;

import com.crmfoodestablishment.userauthservice.ApiErrorInfo;

import com.crmfoodestablishment.userauthservice.authservice.exception.InvalidTokenException;
import com.crmfoodestablishment.userauthservice.authservice.exception.InvalidUserCredentialsException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = {
            InvalidUserCredentialsException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<ApiErrorInfo> handleInvalidAuthInput(
            RuntimeException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(exception.getClass().getName())
                .status(HttpStatus.UNAUTHORIZED)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }
}
