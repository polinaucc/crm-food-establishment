package com.crmfoodestablishment.usermanager.auth.service;

import com.crmfoodestablishment.usermanager.auth.token.AccessToken;
import com.crmfoodestablishment.usermanager.auth.token.RefreshToken;
import com.crmfoodestablishment.usermanager.auth.token.TokenPair;
import com.crmfoodestablishment.usermanager.crud.entity.User;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    TokenPair issueTokenPair(User user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
