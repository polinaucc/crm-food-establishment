package com.crm.food.establishment.user.manager.dto;

import com.crm.food.establishment.user.auth.token.TokenPair;

import java.util.UUID;

public record RegisterUserResponseDto (
        UUID userUuid,
        TokenPair tokenPair
) {}
