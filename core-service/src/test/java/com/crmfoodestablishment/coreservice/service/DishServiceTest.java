package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.DishMapper;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
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
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, onion, seasoning"),
                new CreateDishDto("salad", BigDecimal.valueOf(50.0), "cabbage, salt, potato, cucumber")
        );
    }

    @Test
    void addDishes_ShouldAddNewDishes() {
        UUID menuId = UUID.randomUUID();
        Menu existingMenu = new Menu();
        existingMenu.setUuid(menuId);

        Dish dish = new Dish();
        dish.setMenu(existingMenu);
        dish.setName("barbecue");
        dish.setPrice(BigDecimal.valueOf(150.0));
        dish.setIngredients("meat, onion, seasoning");

        Dish dish1 = new Dish();
        dish1.setMenu(existingMenu);
        dish1.setName("salad");
        dish1.setPrice(BigDecimal.valueOf(50.0));
        dish1.setIngredients("cabbage, salt, potato, cucumber");

        List<Dish> expectedDishes = List.of(dish, dish1);

        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(existingMenu));
        ArgumentCaptor<List<Dish>> dishArgumentCaptor = ArgumentCaptor.forClass(List.class);
        dishServiceImpl.addDishes(menuId, dishesDto);

        verify(dishRepository).saveAll(dishArgumentCaptor.capture());
        List<Dish> actualDishes = dishArgumentCaptor.getValue();
        assertThat(actualDishes).isEqualTo(expectedDishes);
    }

    @Test
    void addDishes_ShouldGetExceptionWhenMenuIdNotExist() {
        UUID menuId = UUID.randomUUID();

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dishServiceImpl.addDishes(menuId, dishesDto))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with id " + menuId + " not exist");
    }

    @Test
    void addDishes_ShouldGetExceptionWhenDishNameExist() {
        UUID menuId = UUID.randomUUID();
        Menu existingMenu = new Menu();
        existingMenu.setUuid(menuId);

        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(existingMenu));
        when(dishRepository.existsByName(dishesDto.get(0).getName())).thenReturn(true);

        assertThatThrownBy(() -> dishServiceImpl.addDishes(menuId, dishesDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dish " + dishesDto.get(0).getName() + " already exists");
    }
}
