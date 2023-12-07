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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService  {

    private final DishRepository dishRepository;

    private final MenuRepository menuRepository;

    private final DishMapper dishMapper = Mappers.getMapper(DishMapper.class);

    @Override
    public List<CreateDishDto> addDishes(UUID menuId, List<CreateDishDto> dishDtos) {
        Menu menu = menuRepository.getMenuByUuid(menuId)
                .orElseThrow(() -> (new MenuNotFoundException("Menu with id " + menuId + " not exist")));

        for (CreateDishDto dishDto: dishDtos) {
            if (dishRepository.existsByName(dishDto.getName())) {
                throw new IllegalArgumentException("Dish " + dishDto.getName() + " already exists");
            }
        }

        List<Dish> dishesToSave = dishMapper.mapDishDtoToDish(dishDtos, menu);

        List<Dish> savedDishes = dishRepository.saveAll(dishesToSave);
        return dishMapper.mapDishToDishDto(savedDishes);
    }
}
