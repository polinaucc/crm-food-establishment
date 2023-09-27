package com.crmfoodestablishment.auth.service;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.auth.service.model.AccessToken;
import com.crmfoodestablishment.auth.service.model.RefreshToken;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    void invalidateRefreshToken(String userEmail);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
