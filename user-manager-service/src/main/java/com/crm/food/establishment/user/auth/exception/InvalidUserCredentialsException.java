package com.crm.food.establishment.user.auth.exception;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "invalid.user.credentials";
    }
}
