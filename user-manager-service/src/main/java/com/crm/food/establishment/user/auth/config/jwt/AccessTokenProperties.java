package com.crm.food.establishment.user.auth.config.jwt;

public record AccessTokenProperties (
        String publicKey,
        String secretKey,
        Long expirationTimeInMinutes
) {}
