package com.crm.food.establishment.user;

import lombok.Builder;

@Builder
public record ApiErrorDTO (
        String code,
        String description
) {}
