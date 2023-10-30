package com.crmfoodestablishment.coreservice.service;

public class MenuNotFoundException extends RuntimeException {
    public MenuNotFoundException(String message) {
        super(message);
    }
}