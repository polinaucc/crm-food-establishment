package com.crm.food.establishment.core.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class CreateDishDto {

    @NotBlank(message = "Field name cannot be blank")
    private String name;

    @NotNull(message = "Field price cannot be null")
    @PositiveOrZero(message = "Price cannot be negative")
    private BigDecimal price;

    @NotBlank(message = "Field ingredients cannot be blank")
    private String ingredients;
}
