package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.service.DishService;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONUtil;
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
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
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
    void shouldGet400BadRequestWhenRequestContainNullValue() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto(null, BigDecimal.valueOf(150.0), "meat, spices")
        );

        when(dishService.addDishes(eq(menuId), anyList())).thenReturn(Collections.emptyList());
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(Collections.singletonList(dishesDto))));

        resultActions.andExpect(status().is4xxClientError());
    }

    @Test
    void whenValidInputThenMapsToBusinessModel() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );

        mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isOk());
        ArgumentCaptor<List<CreateDishDto>> dishCaptor = ArgumentCaptor.forClass(List.class);

        verify(dishService, times(1)).addDishes(eq(menuId), dishCaptor.capture());
        assertThat((dishCaptor.getValue()).size()).isEqualTo(2);
        assertThat((dishCaptor.getValue().get(0).getName())).isEqualTo("barbecue");
        assertThat((dishCaptor.getValue().get(1).getName())).isEqualTo("soup");
    }

    @Test
    void whenValidInputThenReturnDishDto() throws Exception {
        int menuId = 1;
        List<CreateDishDto> dishesDto = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );
        List<CreateDishDto> expectedResponseBody = Arrays.asList(
                new CreateDishDto("barbecue", BigDecimal.valueOf(150.0), "meat, spices"),
                new CreateDishDto("soup", BigDecimal.valueOf(50.0), "water, salt, potato, tomato")
        );

        when(dishService.addDishes(eq(menuId), anyList()))
                .thenReturn(dishesDto);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/dishes/{menuId}", menuId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dishesDto)))
                .andExpect(status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedResponseBody));
    }
}
