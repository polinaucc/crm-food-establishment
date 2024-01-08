package com.crm.food.establishment.core.dto;

import com.crm.food.establishment.core.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class MenuDto {

    private UUID uuid;

    @NotBlank(message = "Field name cannot be blank")
    private String name;

    private String comment;

    @NotNull(message = "Field season cannot be null")
    private Season season;

    private List<CreateDishDto> dishes;
}
