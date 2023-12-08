package com.crm.food.establishment.user;

import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalInputValidationControllerAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<List<ApiErrorInfo>> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        log.error(exception.getMessage(), exception);

        List<ApiErrorInfo> errorInfos = exception.getFieldErrors().stream()
                .map(error -> new ApiErrorInfo(
                        InvalidArgumentException.errorCode(),
                        error.getField() + ": " + error.getDefaultMessage()
                ))
                .toList();

        return new ResponseEntity<>(errorInfos, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiErrorInfo> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        log.error(exception.getMessage(), exception);

        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .code(InvalidArgumentException.errorCode())
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, HttpStatus.BAD_REQUEST);
    }
}
