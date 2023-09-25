package com.crmfoodestablishment.usermanager.security.models;

import java.util.Date;

public record AccessTokenClaims(
        Date issuedAt,
        Date expiration
) {}
