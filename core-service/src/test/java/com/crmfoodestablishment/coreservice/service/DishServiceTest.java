package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.DishMapper;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import static org.hamcrest.Matchers.any;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.anyInt;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @InjectMocks
    private DishService dishService;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private DishMapper dishMapper;

    private static List<CreateDishDto> createDishesDto() {
        return Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, onion, seasoning"),
                new CreateDishDto("salad", BigDecimal.valueOf(50.0), "cabbage, salt, potato, cucumber")
        );
    }

    @Test
    void shouldAddNewDishes() {
        Integer menuId = 1;
        Menu existingMenu = new Menu();
        existingMenu.setId(menuId);

        List<CreateDishDto> dishDtoList = createDishesDto();
        when(menuRepository.getMenuById(menuId)).thenReturn(Optional.ofNullable(existingMenu));
        List<CreateDishDto> returnedDishDtoList = dishService.addDishes(menuId, dishDtoList);

        verify(dishRepository, times(1)).saveAll(anyList());
        assertNotNull(returnedDishDtoList);
    }

    @Test
    void shouldGetExceptionWhenMenuIdNotExist() {
        Integer menuId = 1;
        List<CreateDishDto> dishDtoList = createDishesDto();

        when(menuRepository.getMenuById(anyInt())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dishService.addDishes(menuId, dishDtoList))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with id " + menuId + " not exist");
    }
}
