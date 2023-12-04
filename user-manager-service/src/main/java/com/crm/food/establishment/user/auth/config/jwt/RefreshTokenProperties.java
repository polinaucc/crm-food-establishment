package com.crm.food.establishment.user.auth.config.jwt;

public record RefreshTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTime
) {}
