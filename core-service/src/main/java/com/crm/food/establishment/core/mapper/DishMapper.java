package com.crm.food.establishment.core.mapper;

import com.crm.food.establishment.core.entity.Dish;
import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.entity.Menu;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper
public interface DishMapper {
    List<CreateDishDto> mapDishListToDishDto(List<Dish> dish);

    default List<Dish> mapCreatedDishDtoToDish(List<CreateDishDto> dishDtos, Menu menu) {
        return dishDtos.stream()
                .map(dishDto -> mapCreatedDishDtoToDish(dishDto, menu))
                .toList();
    }

    @Mapping(target = "menu", source = "menu")
    @Mapping(target = "name", source = "createDishDto.name")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "dishes", ignore = true)
    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    Dish mapCreatedDishDtoToDish(CreateDishDto createDishDto, Menu menu);
}
