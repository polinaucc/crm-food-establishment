package com.crmfoodestablishment.userauthservice.authservice.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }
}
