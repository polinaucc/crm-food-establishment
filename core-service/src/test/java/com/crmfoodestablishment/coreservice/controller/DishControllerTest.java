package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.service.DishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private DishController dishController;

    @MockBean
    private DishService dishService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateDishes() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = new ArrayList<>();

        when(dishService.addDishes(eq(menuId), anyList()))
                .thenReturn(dishesDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isOk());
    }

    @Test
    void shouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto(null, BigDecimal.valueOf(150.0), "meat, spices")
        );

        when(dishService.addDishes(eq(menuId), anyList())).thenReturn(Collections.emptyList());

        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(Collections.singletonList(dishesDto))))
                .andExpect(status().is4xxClientError());
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
                .andExpect(status().isOk());
        ArgumentCaptor<List<CreateDishDto>> argumentCaptor = ArgumentCaptor.forClass(List.class);

        verify(dishService, times(1)).addDishes(eq(menuId), argumentCaptor.capture());
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

    @Test
    void shouldGetValidInputThenReturnDishDto() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );
        List<CreateDishDto> expectedResponseBody = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );

        when(dishService.addDishes(eq(menuId), anyList())).thenReturn(dishesDto);
        String actualResponseBody = mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualTo(objectMapper
                .writeValueAsString(expectedResponseBody));
    }
}
