package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import java.util.List;
import java.util.UUID;

public interface DishService {

    List<CreateDishDto> addDishes(UUID menuId, List<CreateDishDto> dishDto);
}
