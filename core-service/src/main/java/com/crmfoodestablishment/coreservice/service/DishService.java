package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import java.util.List;

public interface DishService {

    List<CreateDishDto> addDishes(Integer menuId, List<CreateDishDto> dishDto);
}
