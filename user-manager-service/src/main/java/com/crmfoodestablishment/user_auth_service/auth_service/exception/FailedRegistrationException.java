package com.crmfoodestablishment.user_auth_service.auth_service.exception;

public class FailedRegistrationException extends RuntimeException {

    public FailedRegistrationException(String message) {
        super(message);
    }
}
