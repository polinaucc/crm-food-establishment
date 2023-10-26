package com.crmfoodestablishment.user_auth_service.auth_service.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }
}
