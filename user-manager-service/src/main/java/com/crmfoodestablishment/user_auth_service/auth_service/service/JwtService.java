package com.crmfoodestablishment.user_auth_service.auth_service.service;

import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;
import com.crmfoodestablishment.user_auth_service.auth_service.service.token.AccessToken;
import com.crmfoodestablishment.user_auth_service.auth_service.service.token.RefreshToken;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
