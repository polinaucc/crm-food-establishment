package com.crmfoodestablishment.usermanager.auth.config.jwt;

public record AccessTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTime
) {}
