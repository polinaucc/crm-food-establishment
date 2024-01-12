package com.crm.food.establishment.core;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.List;
import java.util.ArrayList;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<ErrorTemplate> handleValidationException(Exception ex) {
        List<ErrorTemplate> errorInfos = new ArrayList<>();
        String title = "Validation exception";

        switch (ex) {
            case MethodArgumentNotValidException mnve -> {
                mnve.getBindingResult().getFieldErrors()
                        .forEach(fe -> errorInfos.add(new ErrorTemplate(title, String.format("[%s: '%s']",fe.getField(), fe.getDefaultMessage()))));
            }
            case ConstraintViolationException cve -> {
                cve.getConstraintViolations()
                        .forEach(v -> errorInfos.add(new ErrorTemplate(title, String.format("[%s: '%s']", v.getPropertyPath().toString(), v.getMessage()))));
            }
            default -> {
            }
        }
        return errorInfos;
    }
}
