package com.crmfoodestablishment.user_auth_service.auth_sevice.service;

import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.AccessToken;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.RefreshToken;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    void invalidateRefreshToken(String userEmail);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
