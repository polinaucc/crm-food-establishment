package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.DishDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.*;
import java.util.List;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface DishMapper {

    List<DishDto> mapDishToDishDto(List<Dish> dish);

    @Mapping(target = "menu", ignore = true)
    List<Dish> mapDishDtoToDish(List<DishDto> dishDto, @Context Menu menu);

    @AfterMapping
    default void setMenu(@MappingTarget Dish dish, @Context Menu menu) {
        dish.setMenu(menu);
    }
}
