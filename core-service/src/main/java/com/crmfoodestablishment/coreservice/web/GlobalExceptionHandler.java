package com.crmfoodestablishment.coreservice.web;

import com.crmfoodestablishment.coreservice.service.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity handleNotFoundException(NotFoundException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(exception.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity handleInvalidRequestParameters(MethodArgumentNotValidException exception) {
        Map<String, String> badRequestMessages = new HashMap<>();
        exception.getBindingResult().getAllErrors().forEach((badRequestMessage) -> {
            String field = ((FieldError) badRequestMessage).getField();
            String message = badRequestMessage.getDefaultMessage();
            badRequestMessages.put(field, message);
        });
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(badRequestMessages);
    }
}
