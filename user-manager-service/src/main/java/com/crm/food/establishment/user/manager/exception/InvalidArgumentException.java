package com.crm.food.establishment.user.manager.exception;

public class InvalidArgumentException extends RuntimeException {

    public InvalidArgumentException(String message) {
        super(message);
    }

    public static String readableName() {
        return "Invalid argument exception";
    }
}