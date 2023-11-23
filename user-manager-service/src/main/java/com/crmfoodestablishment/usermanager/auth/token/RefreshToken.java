package com.crmfoodestablishment.usermanager.auth.token;

import io.jsonwebtoken.Header;

public record RefreshToken(
        Header header,
        RefreshTokenClaims claims
) {}
