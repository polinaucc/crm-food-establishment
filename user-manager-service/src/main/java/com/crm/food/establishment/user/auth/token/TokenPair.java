package com.crm.food.establishment.user.auth.token;

public record TokenPair (
        String accessToken,
        String refreshToken
) {}
