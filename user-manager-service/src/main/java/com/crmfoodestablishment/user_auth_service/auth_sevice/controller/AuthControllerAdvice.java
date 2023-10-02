package com.crmfoodestablishment.user_auth_service.auth_sevice.controller;

import com.crmfoodestablishment.user_auth_service.ApiErrorInfo;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = {InvalidUserCredentialsException.class})
    public ResponseEntity<ApiErrorInfo> handleWrongUserCredentials(
            InvalidUserCredentialsException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title("Wrong user credentials exception")
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {InvalidTokenException.class})
    public ResponseEntity<ApiErrorInfo> handleInvalidToken(
            InvalidTokenException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title("Invalid token exception")
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {FailedRegistrationException.class})
    public ResponseEntity<ApiErrorInfo> handleRegistration(
            FailedRegistrationException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title("Registration exception")
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }
}
