package com.crmfoodestablishment.paymentservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorTemplate> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error
                -> errors.put(error.getField(), error.getDefaultMessage()));

        ErrorTemplate validationResult = new ErrorTemplate(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Validation failed",
                errors
        );
        return new ResponseEntity<>(validationResult, HttpStatus.BAD_REQUEST);
    }
}
