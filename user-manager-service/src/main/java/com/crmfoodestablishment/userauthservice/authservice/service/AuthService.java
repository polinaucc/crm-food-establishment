package com.crmfoodestablishment.userauthservice.authservice.service;

import com.crmfoodestablishment.userauthservice.authservice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;

import java.util.UUID;

public interface AuthService {

    TokenPair login(LoginRequestPayload credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}
