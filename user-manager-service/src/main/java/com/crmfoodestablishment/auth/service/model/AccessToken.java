package com.crmfoodestablishment.auth.service.model;

import io.jsonwebtoken.Header;

public record AccessToken(Header header, AccessTokenClaims claims) {}
