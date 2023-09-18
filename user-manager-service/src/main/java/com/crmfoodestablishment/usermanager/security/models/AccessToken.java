package com.crmfoodestablishment.usermanager.security.models;

public record AccessToken(Header header, AccessTokenClaims claims) {}
