package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper
public interface DishMapper {
    List<CreateDishDto> mapDishToDishDto(List<Dish> dish);

    default List<Dish> mapDishDtoToDish(List<CreateDishDto> dishDtos, Menu menu) {
        return dishDtos.stream()
                .map(dishDto -> mapDishDtoToDish(dishDto, menu))
                .toList();
    }

    @Mapping(target = "menu", source = "menu")
    @Mapping(target = "name", source = "createDishDto.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    Dish mapDishDtoToDish(CreateDishDto createDishDto, Menu menu);
}
