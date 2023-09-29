package com.crmfoodestablishment.user_auth_service.auth_sevice.exception;

public class WrongUserCredentialsException extends RuntimeException {

    public WrongUserCredentialsException(String message) {
        super(message);
    }
}
