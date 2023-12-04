package com.crm.food.establishment.user;

import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalInputValidationControllerAdvice {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiErrorInfo> handleMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        log.error(exception.getMessage(), exception);

        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(InvalidArgumentException.readableName())
                .status(HttpStatus.BAD_REQUEST)
                .description(
                        MethodArgumentNotValidException.errorsToStringList(exception
                                .getBindingResult()
                                .getAllErrors()
                        ).toString()
                )
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<ApiErrorInfo> handleConstraintViolationException(
            ConstraintViolationException exception
    ) {
        log.error(exception.getMessage(), exception);

        ApiErrorInfo errorInfo = ApiErrorInfo.builder()
                .title(InvalidArgumentException.readableName())
                .status(HttpStatus.BAD_REQUEST)
                .description(exception.getMessage())
                .build();

        return new ResponseEntity<>(errorInfo, errorInfo.getStatus());
    }
}
