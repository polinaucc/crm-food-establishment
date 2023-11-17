package com.crmfoodestablishment.coreservice.web;

import com.crmfoodestablishment.coreservice.dto.order.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.dto.order.DeliveryDetailsDto;
import com.crmfoodestablishment.coreservice.entity.DeliveryMethod;
import com.crmfoodestablishment.coreservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private OrderService orderService;

    private CreateNewOrderDto createNewOrderDtoForTesting() {
        CreateNewOrderDto.DishInOrderDto dishInOrderDto = new CreateNewOrderDto.DishInOrderDto();
        dishInOrderDto.setUuid(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")); // present in a DB
        dishInOrderDto.setAmount((short) 2);
        List<CreateNewOrderDto.DishInOrderDto> testList = new ArrayList();
        testList.add(0, dishInOrderDto);

        DeliveryDetailsDto deliveryDetailsDto = new DeliveryDetailsDto();
        deliveryDetailsDto.setFirstName("qwe");
        deliveryDetailsDto.setLastName("batkovich");
        deliveryDetailsDto.setPhoneNumber("380-96-22-22-111");
        deliveryDetailsDto.setAddress("random address");

        CreateNewOrderDto createNewOrderDto = new CreateNewOrderDto(
                "afee6B5d-6f81-4B19-Ce9b-9FD5Bcf9B92d",
                testList,
                "comment",
                DeliveryMethod.ADDRESS_DELIVERY,
                deliveryDetailsDto
        );
        return createNewOrderDto;
    }

    @Test
    public void shouldReturnNewOrderUuid() throws Exception {
        String jsonRequest = new ObjectMapper().writeValueAsString(createNewOrderDtoForTesting());

        this.mockMvc.perform(post("/order-api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isCreated());

        when(orderService.createOrder(createNewOrderDtoForTesting())).thenReturn(UUID.randomUUID());
    }

    @Test
    public void shouldReturnMessageAbout() throws Exception {
        CreateNewOrderDto createNewOrderDto = createNewOrderDtoForTesting();
        /*"3fa85f64-5717-4562-b3fc-2c963f66afa9" Not present in a DB*/
        createNewOrderDto.getDishes().get(0).setUuid(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa9"));
        String jsonRequest = new ObjectMapper().writeValueAsString(createNewOrderDto);

        this.mockMvc.perform(post("/order-api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Dish is not found"));
    }

    @Test
    public void shouldReturnMessageAboutIncorrectPhoneNumberTestGlobalHandler() throws Exception {
        CreateNewOrderDto createNewOrderDto = createNewOrderDtoForTesting();
        createNewOrderDto.getDeliveryDetails().setPhoneNumber("380.96.11.22.333");
        String jsonRequest = new ObjectMapper().writeValueAsString(createNewOrderDto);

        this.mockMvc.perform(post("/order-api/v1/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"deliveryDetails.phoneNumber\":\"Please inject a correct Ukrainian phone number format, started from 380\"}"));
    }

}