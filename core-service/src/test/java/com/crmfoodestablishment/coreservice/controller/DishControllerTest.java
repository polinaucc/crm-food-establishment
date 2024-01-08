package com.crmfoodestablishment.coreservice.controller;

import com.crm.food.establishment.core.CoreServiceApplication;
import com.crm.food.establishment.core.controller.DishController;
import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.service.DishService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DishController.class)
//@SpringBootTest
@ContextConfiguration(classes= CoreServiceApplication.class)
@ExtendWith(MockitoExtension.class)
class DishControllerTest {

    private static final String CREATE_DISH_URL = "/api/dishes/%s";

    @Autowired
    private MockMvc mockMvc;

    @MockBean //TODO: add mock of the repository
    private DishService dishService;

    @Autowired
    private ObjectMapper objectMapper;

    private List<CreateDishDto> dishesToCreate;



    /**
     * * correct menuId + correct List (2 dishes) - CREATED + 2 uuid in response
     * * correct menuId + List with 1 dish - CREATED + 1 uuid in response
     * * correct menuID + empty List - 400 + validate message
     * * text instead of uuid + List with 1 dish - 400
     * * invalid uuid (not in db) + List with 1 dish - 404
     */
    @BeforeEach
    public void setUpData() {
        dishesToCreate = Arrays.asList(
                new CreateDishDto("Barbecue", BigDecimal.valueOf(150.0), "Meat, spices"),
                new CreateDishDto("Soup", BigDecimal.valueOf(50.0), "Water, salt, potato, tomato")
        );
    }

    @Test
    void createDishes_ShouldSuccessfullyCreateDishes() throws Exception {
        String menuId = UUID.randomUUID().toString();

        List<UUID> expectedResponse = List.of(UUID.randomUUID());
        when(dishService.addDishes(eq(menuId), eq(dishesToCreate))).thenReturn(expectedResponse);

        MockHttpServletRequestBuilder requestBuilder = buildRestRequest(String.format(CREATE_DISH_URL, menuId), dishesToCreate);
        MvcResult result = mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andReturn();

        List<String> actualResponse = objectMapper.readValue(result.getResponse().getContentAsString(), List.class);
        List<UUID> actualUuids = actualResponse.stream().map(UUID::fromString).toList();

        AssertionsForClassTypes.assertThat(actualUuids).isEqualTo(expectedResponse);
    }

//    @Test
//    void createDishes_ShouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
//        UUID menuId = UUID.randomUUID();
//        dishesToCreate.get(0).setName(null);
//
//        when(dishService.addDishes(menuId), anyList())).thenReturn(Collections.emptyList());
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_DISH_URL, menuId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dishesToCreate)))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.title").value("Validation exception"))
//                .andExpect(jsonPath("$.errors[0]").value("[createDishes.dishesDto[0].name: 'Field name cannot be blank']"));
//    }
//
//    @Test
//    void createDishes_ShouldSaveMenuWithCorrectValues() throws Exception {
//        UUID menuId = UUID.randomUUID();
//
//        mockMvc.perform(MockMvcRequestBuilders.post(CREATE_DISH_URL, menuId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(dishesToCreate)))
//                .andExpect(status().isCreated());
//        ArgumentCaptor<List<CreateDishDto>> dishesArgumentCaptor = ArgumentCaptor.forClass(List.class);
//
//        verify(dishService).addDishes(eq(menuId), dishesArgumentCaptor.capture());
//        List<CreateDishDto> actualDishes = dishesArgumentCaptor.getValue();
//        assertThat(actualDishes.size()).isEqualTo(2);
//        assertThat(actualDishes).isEqualTo(dishesToCreate);
//    }

    private MockHttpServletRequestBuilder buildRestRequest(String url, Object requestBody) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }
}
