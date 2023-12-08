package com.crm.food.establishment.user.auth.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }

    public static String errorCode() {
        return "invalid.token";
    }
}
