package com.crmfoodestablishment.usermanager.auth.token;

import com.crmfoodestablishment.usermanager.crud.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub,
        Role role
) {}
