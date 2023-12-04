package com.crm.food.establishment.user.auth.token;

import io.jsonwebtoken.Header;

public record AccessToken(
        Header header,
        AccessTokenClaims claims
) {}
