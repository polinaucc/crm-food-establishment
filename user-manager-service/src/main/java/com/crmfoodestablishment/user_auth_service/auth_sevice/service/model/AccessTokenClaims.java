package com.crmfoodestablishment.user_auth_service.auth_sevice.service.model;

import java.util.Date;

public record AccessTokenClaims(
        Date issuedAt,
        Date expiration
) {}
