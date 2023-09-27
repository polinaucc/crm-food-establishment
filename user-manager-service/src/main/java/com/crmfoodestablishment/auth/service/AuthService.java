package com.crmfoodestablishment.auth.service;

import com.crmfoodestablishment.auth.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.auth.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.auth.controller.payload.TokenPairResponsePayload;
import com.crmfoodestablishment.auth.controller.payload.UserCreationRequestPayload;

public interface AuthService {

    TokenPairResponsePayload login(LoginRequestPayload credentials);

    RegisterResponsePayload register(UserCreationRequestPayload creationData);

    String refresh(String refreshToken);

    void logout(String userEmail);
}
