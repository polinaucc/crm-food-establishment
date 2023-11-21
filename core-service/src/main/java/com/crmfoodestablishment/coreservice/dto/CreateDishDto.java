package com.crmfoodestablishment.coreservice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CreateDishDto {

    @NotBlank(message = "Field name cannot be blank")
    private String name;

    @NotNull(message = "Field price cannot be null")
    private BigDecimal price;

    @NotBlank(message = "Field ingredients cannot be blank")
    private String ingredients;
}
