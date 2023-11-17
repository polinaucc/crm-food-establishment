package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.DishDto;
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
public class DishService {

    private final DishRepository dishRepository;

    private final MenuRepository menuRepository;

    private final DishMapper dishMapper = Mappers.getMapper(DishMapper.class);

    public List<DishDto> addDishes(Integer id, List<DishDto> dishDto) {
        if (!menuRepository.existsById(id)) {
            throw new MenuNotFoundException("Menu with id " + id + " not exist");
        }
        Menu menu = menuRepository.getMenuById(id);

        List<Dish> dishesToSave = dishMapper.mapDishDtoToDish(dishDto, menu);
        List<Dish> savedDishes = dishRepository.saveAll(dishesToSave);

        return dishMapper.mapDishToDishDto(savedDishes);
    }
}
