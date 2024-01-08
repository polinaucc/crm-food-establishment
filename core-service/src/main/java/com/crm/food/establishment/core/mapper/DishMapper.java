package com.crm.food.establishment.core.mapper;

import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.entity.Dish;
import com.crm.food.establishment.core.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper
public interface DishMapper {
    List<CreateDishDto> mapDishListToDishDto(List<Dish> dish);

    default List<Dish> mapCreatedDishDtoToEntity(List<CreateDishDto> dishDtos, Menu menu) {
        return dishDtos.stream()
                .map(dishDto -> mapCreatedDishDtoToEntity(dishDto, menu))
                .toList();
    }

    @Mapping(target = "menu", source = "menu")
    @Mapping(target = "name", source = "createDishDto.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    Dish mapCreatedDishDtoToEntity(CreateDishDto createDishDto, Menu menu);
}
