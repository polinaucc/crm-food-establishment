package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.service.DishServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
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
    private DishServiceImpl dishServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateDishes() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );

        when(dishServiceImpl.addDishes(eq(menuId), anyList()))
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
    void shouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto(null, BigDecimal.valueOf(150.0), "meat, spices")
        );

        when(dishServiceImpl.addDishes(eq(menuId), anyList())).thenReturn(Collections.emptyList());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.errors['createDishes.dishesDto[0].name']").value("Field name cannot be blank"));
    }

    @Test
    void shouldGetValidInputThenMapsToBusinessModel() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isCreated());
        ArgumentCaptor<List<CreateDishDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(dishServiceImpl, times(1)).addDishes(eq(menuId), argumentCaptor.capture());
        List<CreateDishDto> dishCaptorValue = argumentCaptor.getValue();
        assertThat(dishCaptorValue.size()).isEqualTo(2);
        for (int i = 0; i < dishCaptorValue.size(); i++) {
            CreateDishDto actualDishDto = dishCaptorValue.get(i);
            CreateDishDto expectedDishDto = dishesDto.get(i);
            assertEquals(expectedDishDto.getName(), actualDishDto.getName());
            assertEquals(expectedDishDto.getPrice(), actualDishDto.getPrice());
            assertEquals(expectedDishDto.getIngredients(), actualDishDto.getIngredients());
        }
    }
}
