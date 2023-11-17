package com.crmfoodestablishment.userauthservice.authservice.service;

import com.crmfoodestablishment.userauthservice.authservice.token.AccessToken;
import com.crmfoodestablishment.userauthservice.authservice.token.RefreshToken;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import com.crmfoodestablishment.userauthservice.usermanager.entity.User;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    TokenPair issueTokenPair(User user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
