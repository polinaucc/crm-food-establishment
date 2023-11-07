package com.crmfoodestablishment.userauthservice.authservice.config.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "auth.jwt")
public record JwtProperties (
        AccessTokenProperties accessToken,
        RefreshTokenProperties refreshToken
) {}
