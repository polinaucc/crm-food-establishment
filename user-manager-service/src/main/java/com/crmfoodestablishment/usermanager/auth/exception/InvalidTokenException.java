package com.crmfoodestablishment.usermanager.auth.exception;

public class InvalidTokenException extends RuntimeException{

    public InvalidTokenException(String message) {
        super(message);
    }

    public static String readableName() {
        return "Invalid token exception";
    }
}
