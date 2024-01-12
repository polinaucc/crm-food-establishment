package com.crm.food.establishment.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import java.math.BigDecimal;
import java.util.UUID;
public record CreateDishDto(
        UUID uuid,
        @NotBlank(message = "Field name cannot be blank")
        String name,
        @NotNull(message = "Field price cannot be null")
        @PositiveOrZero(message = "Price cannot be negative")
        BigDecimal price,
        @NotBlank(message = "Field ingredients cannot be blank")
        String ingredients) {
}
