package com.crmfoodestablishment.usermanager.auth.token;

import io.jsonwebtoken.Header;

public record AccessToken(
        Header header,
        AccessTokenClaims claims
) {}
