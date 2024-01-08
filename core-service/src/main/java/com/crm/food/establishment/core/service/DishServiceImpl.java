package com.crm.food.establishment.core.service;

import com.crm.food.establishment.core.entity.Dish;
import com.crm.food.establishment.core.entity.Menu;
import com.crm.food.establishment.core.repository.DishRepository;
import com.crm.food.establishment.core.repository.MenuRepository;
import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.mapper.DishMapper;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DishServiceImpl implements DishService {

    private final DishRepository dishRepository;
    private final MenuRepository menuRepository;
    private final DishMapper dishMapper = Mappers.getMapper(DishMapper.class);

    @Override
    public List<UUID> addDishes(String menuId, List<CreateDishDto> dtoToCreateDishes) {
        Menu menu = menuRepository.getMenuByUuid(UUID.fromString(menuId))
                .orElseThrow(() -> new MenuNotFoundException("Menu with uuid [" + menuId + "] is not found"));

        dtoToCreateDishes.stream()
                .filter(dishDto -> dishRepository.existsByName(dishDto.getName()))
                .findAny()
                .ifPresent(existingDishDto -> {
                    throw new IllegalArgumentException("Dish with name [" + existingDishDto.getName() + "] already exists");
                });

        List<Dish> dishesToSave = dishMapper.mapCreatedDishDtoToEntity(dtoToCreateDishes, menu);
        List<Dish> savedDishes = dishRepository.saveAll(dishesToSave);

        return savedDishes.stream().map(Dish::getUuid).toList();
    }
}
