package com.crmfoodestablishment.auth.exception;

public class WrongUserCredentialsException extends RuntimeException {

    public WrongUserCredentialsException(String message) {
        super(message);
    }
}
