package com.crmfoodestablishment.user_auth_service.auth_sevice.service;

import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.TokenPairResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserCreationRequestPayload;

public interface AuthService {

    TokenPairResponsePayload login(LoginRequestPayload credentials);

    RegisterResponsePayload register(UserCreationRequestPayload creationData);

    String refresh(String refreshToken);

    void logout(String userEmail);
}
