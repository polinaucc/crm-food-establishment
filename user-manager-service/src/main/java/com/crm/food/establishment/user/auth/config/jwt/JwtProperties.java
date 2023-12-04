package com.crm.food.establishment.user.auth.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties (
        AccessTokenProperties accessToken,
        RefreshTokenProperties refreshToken
) {}
