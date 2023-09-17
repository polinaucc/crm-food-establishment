package com.crmfoodestablishment.usermanager.exceptions;

public class WrongUserCredentialsException extends RuntimeException {
    public WrongUserCredentialsException() {
        super();
    }

    public WrongUserCredentialsException(String message) {
        super(message);
    }

    public WrongUserCredentialsException(String message, Throwable cause) {
        super(message, cause);
    }

    public WrongUserCredentialsException(Throwable cause) {
        super(cause);
    }

    protected WrongUserCredentialsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
