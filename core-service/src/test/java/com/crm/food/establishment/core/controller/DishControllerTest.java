package com.crm.food.establishment.core.controller;

import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.service.DishService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Collections;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    private static final String CREATE_DISH_URL = "/api/dishes/%s";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DishService dishService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CreateDishDto> dishesDto;

    private String menuId;

    @BeforeEach
    public void setUpData() {
        menuId = UUID.randomUUID().toString();
        dishesDto = Arrays.asList(
                new CreateDishDto(null, "Barbecue", BigDecimal.valueOf(150.0), "Meat, spices"),
                new CreateDishDto(null, "Soup", BigDecimal.valueOf(50.0), "Water, salt, potato, tomato")
        );
    }

    @Test
    void createDishes_ShouldSuccessfullyCreateDishes() throws Exception {
        List<UUID> expectedUuids = List.of(UUID.randomUUID(), UUID.randomUUID());
        when(dishService.addDishes(menuId, dishesDto)).thenReturn(expectedUuids);

        MockHttpServletRequestBuilder requestBuilder = buildRestRequest(String.format(CREATE_DISH_URL, menuId), dishesDto);
        MvcResult response = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        List<String> actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), List.class);
        List<UUID> actualUuids = actualResponse.stream().map(UUID::fromString).toList();

        assertThat(actualUuids).isEqualTo(expectedUuids);
    }

    @Test
    void createDishes_ShouldSuccessfullyCreateDish() throws Exception {
        List<UUID> expectedUuid = Collections.singletonList(UUID.randomUUID());

        List<CreateDishDto> barbecueDishDto = Collections.singletonList(dishesDto.getFirst());
        when(dishService.addDishes(menuId, barbecueDishDto)).thenReturn(expectedUuid);

        MockHttpServletRequestBuilder requestBuilder = buildRestRequest(String.format(CREATE_DISH_URL, menuId), barbecueDishDto);
        MvcResult response = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        List<String> actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), List.class);
        List<UUID> actualUuid = actualResponse.stream().map(UUID::fromString).toList();

        assertThat(actualUuid).isEqualTo(expectedUuid);
    }

    @Test
    void createDishes_ShouldThrow400BadRequestWhenRequestEmptyList() throws Exception {
        List<CreateDishDto> emptyList = new ArrayList<>();

        when(dishService.addDishes(menuId, emptyList)).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder requestBuilder = buildRestRequest(String.format(CREATE_DISH_URL, menuId), emptyList);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[createDishes.dishesDto: 'must not be empty']"));
    }

    @Test
    void createDishes_ShouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        List<CreateDishDto> createDishDto = Collections.singletonList(new CreateDishDto(null, null, BigDecimal.valueOf(150.0), "Water, salt, potato, tomato"));

        when(dishService.addDishes(menuId, createDishDto)).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder requestBuilder = buildRestRequest(String.format(CREATE_DISH_URL, menuId), createDishDto);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[createDishes.dishesDto[0].name: 'Field name cannot be blank']"));
    }

    @Test
    void createDishes_ShouldThrow400BadRequestWhenMenuIdSomeText() throws Exception {
        menuId = "test";

        when(dishService.addDishes(menuId, dishesDto)).thenReturn(Collections.emptyList());

        MockHttpServletRequestBuilder requestBuilder = buildRestRequest(String.format(CREATE_DISH_URL, menuId), dishesDto);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[createDishes.menuId: 'must be a valid UUID']"));
    }

    private MockHttpServletRequestBuilder buildRestRequest(String url, Object requestBody) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }
}
