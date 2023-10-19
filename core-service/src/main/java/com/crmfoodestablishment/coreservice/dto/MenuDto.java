package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.Season;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuDto {

    private UUID uuid = UUID.randomUUID();

    @NotNull
    @NotBlank
    private String name;

    private String comment;

    @NotNull
    private Season season;

    private List<DishDto> dishes;
}
