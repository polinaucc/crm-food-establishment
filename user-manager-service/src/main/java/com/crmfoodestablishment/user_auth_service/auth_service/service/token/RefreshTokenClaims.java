package com.crmfoodestablishment.user_auth_service.auth_service.service.token;

import java.time.LocalDateTime;
import java.util.UUID;

public record RefreshTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub
) {}
