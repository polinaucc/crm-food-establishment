package com.crmfoodestablishment.usermanager.auth.controller;

import com.crmfoodestablishment.usermanager.ApiErrorInfo;

import com.crmfoodestablishment.usermanager.auth.exception.InvalidTokenException;
import com.crmfoodestablishment.usermanager.auth.exception.InvalidUserCredentialsException;

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
