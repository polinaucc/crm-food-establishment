package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.*;
import java.util.List;

@Mapper
public interface DishMapper {

    List<CreateDishDto> mapDishToDishDto(List<Dish> dish);

    @Mapping(target = "menu", source = "menu")
    List<Dish> mapDishDtoToDish(List<CreateDishDto> dishDto, @Context Menu menu);

    @ObjectFactory
    default Dish createDish(CreateDishDto dishDto, @Context Menu menu) {
        Dish dish = new Dish();
        dish.setMenu(menu);
        return dish;
    }
}
