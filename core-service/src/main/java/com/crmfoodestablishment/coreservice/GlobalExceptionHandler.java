package com.crmfoodestablishment.coreservice;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorTemplate> handleValidationException(Exception ex) {
        String title = "Validation exception";
        List<String> errors = new ArrayList<>();

        if (ex instanceof MethodArgumentNotValidException validationException) {
            List<FieldError> fieldErrors = validationException.getBindingResult().getFieldErrors();
            for (FieldError fieldError: fieldErrors) {
                String field = fieldError.getField();
                String message = fieldError.getDefaultMessage();
                errors.add(String.format("[%s: '%s']", field, message));
            }

        } else if (ex instanceof ConstraintViolationException validationException) {
            Set<ConstraintViolation<?>> violations = validationException.getConstraintViolations();
            for (ConstraintViolation<?> violation: violations) {
                String field = violation.getPropertyPath().toString();
                String message = violation.getMessage();
                errors.add(String.format("[%s: '%s']", field, message));
            }
        }

        ErrorTemplate validationResult = new ErrorTemplate(
                title,
                errors
        );

        return new ResponseEntity<>(validationResult, HttpStatus.BAD_REQUEST);
    }
}
