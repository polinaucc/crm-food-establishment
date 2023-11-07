package com.crmfoodestablishment.userauthservice.authservice.service;

import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.authservice.token.AccessToken;
import com.crmfoodestablishment.userauthservice.authservice.token.RefreshToken;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(UserDTO user);

    String issueRefreshToken(UserDTO user);

    TokenPair issueTokenPair(UserDTO user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
