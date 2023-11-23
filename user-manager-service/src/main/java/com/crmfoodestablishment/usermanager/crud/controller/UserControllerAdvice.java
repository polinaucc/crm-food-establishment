package com.crmfoodestablishment.usermanager.crud.controller;

import com.crmfoodestablishment.usermanager.ApiErrorInfo;
import com.crmfoodestablishment.usermanager.crud.exception.InvalidArgumentException;
import com.crmfoodestablishment.usermanager.crud.exception.NotFoundException;

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
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(InvalidArgumentException.readableName())
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiErrorInfo> handleNotFound(
            NotFoundException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(NotFoundException.readableName())
                .status(HttpStatus.NOT_FOUND)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }
}
