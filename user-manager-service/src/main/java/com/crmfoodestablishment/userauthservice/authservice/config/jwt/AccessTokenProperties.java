package com.crmfoodestablishment.userauthservice.authservice.config.jwt;

public record AccessTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTime
) {}
