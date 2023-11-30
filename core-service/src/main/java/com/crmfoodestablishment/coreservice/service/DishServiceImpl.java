package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.DishMapper;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService  {

    private final DishRepository dishRepository;

    private final MenuRepository menuRepository;

    private final DishMapper dishMapper = Mappers.getMapper(DishMapper.class);

    @Override
    public List<CreateDishDto> addDishes(Integer menuId, List<CreateDishDto> dishDto) {
        Menu menu = menuRepository.getMenuById(menuId)
                .orElseThrow(() -> (new MenuNotFoundException("Menu with id " + menuId + " not exist")));

        List<Dish> dishesToSave = dishMapper.mapDishDtoToDish(dishDto, menu);
        List<Dish> savedDishes = dishRepository.saveAll(dishesToSave);

        return dishMapper.mapDishToDishDto(savedDishes);
    }
}
