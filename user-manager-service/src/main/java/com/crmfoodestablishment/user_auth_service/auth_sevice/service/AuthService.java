package com.crmfoodestablishment.user_auth_service.auth_sevice.service;

import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.TokenPairResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserRegistrationRequestPayload;

import java.util.UUID;

public interface AuthService {

    TokenPairResponsePayload login(LoginRequestPayload credentials);

    RegisterResponsePayload register(UserRegistrationRequestPayload creationData);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}