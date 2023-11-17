package com.crmfoodestablishment.userauthservice.usermanager.controller;

import com.crmfoodestablishment.userauthservice.ApiErrorInfo;
import com.crmfoodestablishment.userauthservice.usermanager.exception.InvalidArgumentException;
import com.crmfoodestablishment.userauthservice.usermanager.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler(value = {InvalidArgumentException.class})
    public ResponseEntity<ApiErrorInfo> handleInvalidArguments(
            InvalidArgumentException exception
    ) {
        ApiErrorInfo errorInfo = mapExceptionToApiErrorInfo(
                exception,
                HttpStatus.BAD_REQUEST
        );

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiErrorInfo> handleNotFound(
            NotFoundException exception
    ) {
        ApiErrorInfo errorInfo = mapExceptionToApiErrorInfo(
                exception,
                HttpStatus.NOT_FOUND
        );

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    private ApiErrorInfo mapExceptionToApiErrorInfo(
            RuntimeException exception,
            HttpStatus status
    ) {
        return ApiErrorInfo.builder()
                .title(exception.getClass().getName())
                .status(status)
                .description(exception.getMessage())
                .build();
    }
}
