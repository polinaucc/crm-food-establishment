package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.service.DishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CreateDishDto> dishesDto;

    @BeforeEach
    public void setUpData() {
        dishesDto = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );
    }

    @Test
    void createDishes_ShouldSuccessfullyCreateDishes() throws Exception {
        UUID menuId = UUID.randomUUID();

        when(dishService.addDishes(eq(menuId), anyList()))
                .thenReturn(dishesDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(dishesDto.get(0).getName()))
                .andExpect(jsonPath("$[0].price").value(dishesDto.get(0).getPrice()))
                .andExpect(jsonPath("$[0].ingredients").value(dishesDto.get(0).getIngredients()))
                .andExpect(jsonPath("$[1].name").value(dishesDto.get(1).getName()))
                .andExpect(jsonPath("$[1].price").value(dishesDto.get(1).getPrice()))
                .andExpect(jsonPath("$[1].ingredients").value(dishesDto.get(1).getIngredients()));
    }

    @Test
    void createDishes_ShouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        UUID menuId = UUID.randomUUID();
        dishesDto.get(0).setName(null);

        when(dishService.addDishes(eq(menuId), anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Validation exception"))
                .andExpect(jsonPath("$.errors[0]").value("[createDishes.dishesDto[0].name: 'Field name cannot be blank']"));
    }

    @Test
    void createDishes_ShouldSaveMenuWithCorrectValues() throws Exception {
        UUID MenuId = UUID.randomUUID();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", MenuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isCreated());
        ArgumentCaptor<List<CreateDishDto>> dishesArgumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(dishService).addDishes(eq(MenuId), dishesArgumentCaptor.capture());
        List<CreateDishDto> actualDishes = dishesArgumentCaptor.getValue();
        assertThat(actualDishes.size()).isEqualTo(2);
        assertThat(actualDishes).isEqualTo(dishesDto);
    }
}
