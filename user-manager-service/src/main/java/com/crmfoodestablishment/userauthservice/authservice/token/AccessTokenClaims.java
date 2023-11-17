package com.crmfoodestablishment.userauthservice.authservice.token;

import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub,
        Role role
) {}
