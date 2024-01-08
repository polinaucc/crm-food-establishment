package com.crm.food.establishment.core.service;

import com.crm.food.establishment.core.dto.CreateDishDto;
import java.util.List;
import java.util.UUID;

public interface DishService {

    List<UUID> addDishes(String menuId, List<CreateDishDto> dishDto);
}
