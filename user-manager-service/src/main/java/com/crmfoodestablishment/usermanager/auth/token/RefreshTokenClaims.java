package com.crmfoodestablishment.usermanager.auth.token;

import java.time.LocalDateTime;
import java.util.UUID;

public record RefreshTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub
) {}
