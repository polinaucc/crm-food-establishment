package com.crmfoodestablishment.userauthservice.authservice.exception;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }
}
