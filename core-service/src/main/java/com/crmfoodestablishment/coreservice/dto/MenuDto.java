package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MenuDto {

    private UUID uuid;

    @NotNull(message = "The name must not be null.")
    @NotBlank
    private String name;

    private String comment;

    @NotNull(message = "The season must not be null.")
    private Season season;

    private List<DishDto> dishes;
}
