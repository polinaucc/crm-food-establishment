package com.crmfoodestablishment.usermanager.auth.exception;

public class InvalidUserCredentialsException extends RuntimeException {

    public InvalidUserCredentialsException(String message) {
        super(message);
    }

    public static String readableName() {
        return "Invalid user credentials exception";
    }
}
