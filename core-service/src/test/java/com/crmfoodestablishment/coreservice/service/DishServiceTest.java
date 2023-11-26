package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.DishMapper;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

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
        Dish dish = new Dish();
        Dish dish1 = new Dish();
        dish.setMenu(existingMenu);
        dish.setName("barbecue");
        dish.setPrice(BigDecimal.valueOf(150.0));
        dish.setIngredients("meat, onion, seasoning");
        dish1.setMenu(existingMenu);
        dish1.setName("salad");
        dish1.setPrice(BigDecimal.valueOf(50.0));
        dish1.setIngredients("cabbage, salt, potato, cucumber");
        List<Dish> dishesList = List.of(dish, dish1);
        List<CreateDishDto> dishDtoList = createDishesDto();

        when(menuRepository.getMenuById(menuId)).thenReturn(Optional.of(existingMenu));
        ArgumentCaptor<List<Dish>> argumentCaptor = ArgumentCaptor.forClass(List.class);
        dishService.addDishes(menuId, dishDtoList);

        verify(dishRepository, times(1)).saveAll(argumentCaptor.capture());
        List<Dish> dishCaptorValue = argumentCaptor.getValue();
        for (int i = 0; i < dishCaptorValue.size(); i++) {
            Dish expectedDish = dishesList.get(i);
            Dish actualDish = dishCaptorValue.get(i);
            assertEquals(expectedDish.getName(), actualDish.getName());
            assertEquals(expectedDish.getPrice(), actualDish.getPrice());
            assertEquals(expectedDish.getIngredients(), actualDish.getIngredients());
        }
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
