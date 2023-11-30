package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MenuDto {

    private UUID uuid;

    @NotBlank(message = "Field name cannot be blank")
    private String name;

    private String comment;

    @NotNull(message = "Field season cannot be null")
    private Season season;

    private List<CreateDishDto> dishes;
}
