package com.crm.food.establishment.user.auth.token;

import java.time.LocalDateTime;
import java.util.UUID;

public record RefreshTokenClaims(
        LocalDateTime iat,
        LocalDateTime exp,
        UUID sub
) {}
