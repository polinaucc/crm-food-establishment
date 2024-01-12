package com.crm.food.establishment.core.dto;

import com.crm.food.establishment.core.entity.Season;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@NoArgsConstructor
public class MenuDto {
    UUID uuid;

    @NotBlank(message = "Field name cannot be blank")
    String name;

    String comment;

    @NotNull(message = "Field season cannot be null")
    Season season;

    List<CreateDishDto> dishes;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuDto menuDto = (MenuDto) o;
        return Objects.equals(name, menuDto.name) && Objects.equals(comment, menuDto.comment) && season == menuDto.season;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, comment, season);
    }
}
