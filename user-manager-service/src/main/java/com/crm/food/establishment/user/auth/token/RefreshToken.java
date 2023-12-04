package com.crm.food.establishment.user.auth.token;

import io.jsonwebtoken.Header;

public record RefreshToken(
        Header header,
        RefreshTokenClaims claims
) {}
