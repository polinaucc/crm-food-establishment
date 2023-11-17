package com.crmfoodestablishment.userauthservice.authservice.service;

import com.crmfoodestablishment.userauthservice.authservice.dto.CredentialsDTO;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;

import java.util.UUID;

public interface AuthService {

    TokenPair login(CredentialsDTO credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}
