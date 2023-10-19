package com.crmfoodestablishment.user_auth_service.auth_sevice.service.token;

import com.crmfoodestablishment.user_auth_service.user_manager.entity.Permission;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record AccessTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub,
        List<Permission> permissions
) {}
