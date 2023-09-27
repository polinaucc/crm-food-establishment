package com.crmfoodestablishment.auth.service.model;

import java.util.Date;

public record AccessTokenClaims(
        Date issuedAt,
        Date expiration
) {}
