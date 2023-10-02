package com.crmfoodestablishment.user_auth_service.auth_sevice.exception;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }
}
