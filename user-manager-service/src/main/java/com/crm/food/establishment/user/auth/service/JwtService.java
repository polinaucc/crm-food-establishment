package com.crm.food.establishment.user.auth.service;

import com.crm.food.establishment.user.auth.token.AccessToken;
import com.crm.food.establishment.user.auth.token.RefreshToken;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.entity.User;

import java.util.UUID;

public interface JwtService {

    String issueAccessToken(User user);

    String issueRefreshToken(User user);

    TokenPair issueTokenPair(User user);

    void invalidateRefreshToken(UUID userUuid);

    AccessToken parseAccessToken(String accessToken);

    RefreshToken parseRefreshToken(String refreshToken);
}
