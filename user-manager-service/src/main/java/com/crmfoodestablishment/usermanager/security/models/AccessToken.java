package com.crmfoodestablishment.usermanager.security.models;

import io.jsonwebtoken.Header;

public record AccessToken(Header header, AccessTokenClaims claims) {}
