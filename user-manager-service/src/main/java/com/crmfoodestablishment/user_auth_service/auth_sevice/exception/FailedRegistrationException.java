package com.crmfoodestablishment.user_auth_service.auth_sevice.exception;

public class FailedRegistrationException extends RuntimeException {

    public FailedRegistrationException(String message) {
        super(message);
    }
}
