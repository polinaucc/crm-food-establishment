package com.crmfoodestablishment.userauthservice.authservice.controller;

import com.crmfoodestablishment.userauthservice.ApiErrorInfo;
import com.crmfoodestablishment.userauthservice.authservice.exception.*;

import com.crmfoodestablishment.userauthservice.usermanager.exception.FailedRegistrationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//TODO add handling of jakarta.validation.constraints
@RestControllerAdvice
public class AuthControllerAdvice {

    @ExceptionHandler(value = {InvalidUserCredentialsException.class})
    public ResponseEntity<ApiErrorInfo> handleWrongUserCredentials(
            InvalidUserCredentialsException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title("Wrong user credentials exception")
                .status(HttpStatus.UNAUTHORIZED)
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
