package com.crmfoodestablishment.coreservice.service;

import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.entity.Dish;
import com.crm.food.establishment.core.entity.Menu;
import com.crm.food.establishment.core.mapper.DishMapper;
import com.crm.food.establishment.core.repository.DishRepository;
import com.crm.food.establishment.core.repository.MenuRepository;
import com.crm.food.establishment.core.service.DishServiceImpl;
import com.crm.food.establishment.core.service.MenuNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DishServiceTest {

    @InjectMocks
    private DishServiceImpl dishServiceImpl;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private DishMapper dishMapper;

    private List<CreateDishDto> dishesDto;

    @BeforeEach
    public void setUpData() {
        dishesDto = Arrays.asList(
                new CreateDishDto("Pasta", BigDecimal.valueOf(152.50), "Pasta, tomatoes, onion"),
                new CreateDishDto("Salad", BigDecimal.valueOf(57.00), "Cabbage, salt, potato, cucumber")
        );
    }

    @Test
    void addDishes_ShouldAddNewDishes() {
        UUID menuId = UUID.randomUUID();

        Menu existingMenu = new Menu();
        existingMenu.setUuid(menuId);

        Dish pastaDish = formDish(existingMenu, "Pasta", 152.50, "Pasta, tomatoes, onion");
        Dish saladDish = formDish(existingMenu, "Salad", 57.00, "Cabbage, salt, potato, cucumber");
        List<Dish> expectedDishes = List.of(pastaDish, saladDish);

        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(existingMenu));
        ArgumentCaptor<List<Dish>> dishArgumentCaptor = ArgumentCaptor.forClass(List.class);

        dishServiceImpl.addDishes(menuId.toString(), dishesDto);

        verify(dishRepository).saveAll(dishArgumentCaptor.capture());

        List<Dish> actualDishes = dishArgumentCaptor.getValue();
        assertThat(actualDishes).isEqualTo(expectedDishes);
    }

    private static Dish formDish(Menu existingMenu, String name, Double price, String ingredients) {
        Dish dish = new Dish();
        dish.setMenu(existingMenu);
        dish.setName(name);
        dish.setPrice(BigDecimal.valueOf(price));
        dish.setIngredients(ingredients);
        return dish;
    }

    @Test
    void addDishes_ShouldGetExceptionWhenMenuIdNotExist() {
        UUID menuId = UUID.randomUUID();

        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dishServiceImpl.addDishes(menuId.toString(), dishesDto))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with uuid [" + menuId + "] is not found");
    }

    @Test
    void addDishes_ShouldGetExceptionWhenDishNameExist() {
        UUID menuId = UUID.randomUUID();

        Menu existingMenu = new Menu();
        existingMenu.setUuid(menuId);

        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(existingMenu));
        when(dishRepository.existsByName(dishesDto.get(0).getName())).thenReturn(true);

        assertThatThrownBy(() -> dishServiceImpl.addDishes(menuId.toString(), dishesDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dish with name [" + dishesDto.get(0).getName() + "] already exists");
    }
}
