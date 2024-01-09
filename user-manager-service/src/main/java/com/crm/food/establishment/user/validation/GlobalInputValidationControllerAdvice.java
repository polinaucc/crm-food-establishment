package com.crm.food.establishment.user.validation;

import com.crm.food.establishment.user.ApiErrorDTO;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
public class GlobalInputValidationControllerAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ApiErrorDTO>> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<ApiErrorDTO> errorInfos = exception.getFieldErrors().stream()
                .map(error -> new ApiErrorDTO(
                        InvalidArgumentException.errorCode(),
                        error.getField() + ": " + error.getDefaultMessage()
                ))
                .toList();

        return new ResponseEntity<>(errorInfos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiErrorDTO> handleConstraintViolationException(ConstraintViolationException exception) {
        ApiErrorDTO errorInfo = ApiErrorDTO.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getConstraintViolations().iterator().next().getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
