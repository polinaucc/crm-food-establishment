package com.crmfoodestablishment.usermanager.auth.service;

import com.crmfoodestablishment.usermanager.auth.dto.CredentialsDTO;
import com.crmfoodestablishment.usermanager.auth.token.TokenPair;

import java.util.UUID;

public interface AuthService {

    TokenPair login(CredentialsDTO credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}
