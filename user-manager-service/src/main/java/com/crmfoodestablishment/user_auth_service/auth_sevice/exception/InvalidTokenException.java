package com.crmfoodestablishment.user_auth_service.auth_sevice.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }
}
