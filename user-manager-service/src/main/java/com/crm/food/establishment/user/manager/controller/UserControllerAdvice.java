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
