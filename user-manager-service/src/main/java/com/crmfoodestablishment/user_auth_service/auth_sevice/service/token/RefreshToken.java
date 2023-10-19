package com.crmfoodestablishment.user_auth_service.auth_sevice.service.token;

import io.jsonwebtoken.Header;

public record RefreshToken(
        Header header,
        RefreshTokenClaims claims
) {}
