package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.ApiErrorDTO;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserControllerAdvice {

    @ExceptionHandler({InvalidArgumentException.class})
    public ResponseEntity<ApiErrorDTO> handleInvalidArguments(InvalidArgumentException exception) {
        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<ApiErrorDTO> handleNotFound(NotFoundException exception) {
        ApiErrorDTO errorDTO = ApiErrorDTO.builder()
                .code(NotFoundException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.NOT_FOUND);
    }
}
