package com.crmfoodestablishment.userauthservice.authservice.config.jwt;

public record RefreshTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTime
) {}
