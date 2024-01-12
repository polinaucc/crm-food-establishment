package com.crm.food.establishment.core.service;

import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.entity.Dish;
import com.crm.food.establishment.core.mapper.DishMapper;
import com.crm.food.establishment.core.repository.MenuRepository;
import com.crm.food.establishment.core.entity.Menu;
import com.crm.food.establishment.core.repository.DishRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.Optional;
import java.util.Arrays;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
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

    private UUID menuId;

    private Menu existingMenu;

    private static Dish formDish(Menu existingMenu, String name, Double price, String ingredients) {
        Dish dish = new Dish();
        dish.setMenu(existingMenu);
        dish.setName(name);
        dish.setPrice(BigDecimal.valueOf(price));
        dish.setIngredients(ingredients);
        return dish;
    }

    @BeforeEach
    public void setUpData() {
        menuId = UUID.randomUUID();

        existingMenu = new Menu();
        existingMenu.setUuid(menuId);

        dishesDto = Arrays.asList(
                new CreateDishDto(null, "Barbecue", BigDecimal.valueOf(150.0), "Meat, onion, seasoning"),
                new CreateDishDto(null, "Salad", BigDecimal.valueOf(50.0), "Cabbage, salt, potato, cucumber")
        );
    }

    @Test
    void addDishes_ShouldAddNewDishes() {
        Dish barbecueDish = formDish(existingMenu, "Barbecue", 150.0, "Meat, onion, seasoning");
        Dish saladDish = formDish(existingMenu, "Salad", 50.00, "Cabbage, salt, potato, cucumber");
        List<Dish> expectedDishes = List.of(barbecueDish, saladDish);

        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(existingMenu));
        ArgumentCaptor<List<Dish>> dishArgumentCaptor = ArgumentCaptor.forClass(List.class);

        dishServiceImpl.addDishes(menuId.toString(), dishesDto);

        verify(dishRepository).saveAll(dishArgumentCaptor.capture());

        List<Dish> actualDishes = dishArgumentCaptor.getValue();
        assertThat(actualDishes).isEqualTo(expectedDishes);
    }

    @Test
    void addDishes_ShouldGetExceptionWhenMenuIdNotExist() {
        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> dishServiceImpl.addDishes(menuId.toString(), dishesDto))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with uuid [" + menuId + "] is not found");
    }

    @Test
    void addDishes_ShouldGetExceptionWhenDishNameExist() {
        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(existingMenu));
        when(dishRepository.existsByName(dishesDto.getFirst().name())).thenReturn(true);

        assertThatThrownBy(() -> dishServiceImpl.addDishes(menuId.toString(), dishesDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Dish with name [" + dishesDto.getFirst().name() + "] already exists");
    }
}
