package com.crmfoodestablishment.usermanager.security;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.usermanager.security.models.AccessToken;
import com.crmfoodestablishment.usermanager.security.models.RefreshToken;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    boolean validateAccessToken(String accessToken);

    boolean validateRefreshToken(String refreshToken);

    void invalidateRefreshToken(String userEmail);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
