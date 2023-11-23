package com.crmfoodestablishment.usermanager.crud.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }

    public static String readableName() {
        return "Not found exception";
    }
}
