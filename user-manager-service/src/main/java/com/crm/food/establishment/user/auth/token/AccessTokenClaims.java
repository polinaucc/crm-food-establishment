package com.crm.food.establishment.user.auth.token;

import com.crm.food.establishment.user.manager.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record AccessTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub,
        Role role
) {}
