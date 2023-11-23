package com.crmfoodestablishment.usermanager.auth.config.jwt;

public record RefreshTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTime
) {}
