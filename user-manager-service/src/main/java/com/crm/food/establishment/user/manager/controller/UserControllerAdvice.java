package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.ApiErrorInfo;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;

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
                .code(InvalidArgumentException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {NotFoundException.class})
    public ResponseEntity<ApiErrorInfo> handleNotFound(
            NotFoundException exception
    ) {
        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .code(NotFoundException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.NOT_FOUND);
    }
}
