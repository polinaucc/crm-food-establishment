package com.crmfoodestablishment.userauthservice.authservice.token;

import io.jsonwebtoken.Header;

public record AccessToken(
        Header header,
        AccessTokenClaims claims
) {}
