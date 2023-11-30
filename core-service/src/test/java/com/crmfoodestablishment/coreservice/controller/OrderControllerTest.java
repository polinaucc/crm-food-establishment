package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.dto.DeliveryDetailsDto;
import com.crmfoodestablishment.coreservice.dto.DishInOrderDto;
import com.crmfoodestablishment.coreservice.entity.DeliveryMethod;
import com.crmfoodestablishment.coreservice.service.OrderService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@WebMvcTest(OrderController.class)
@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    private List<DishInOrderDto> getDishInOrderDto() {
        DishInOrderDto dishInOrderDto = new DishInOrderDto();
        dishInOrderDto.setUuid(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6")); // present in a DB
        dishInOrderDto.setAmount((short) 2);

        List<DishInOrderDto> dishInOrderDtoList = new ArrayList<>();
        dishInOrderDtoList.add(0, dishInOrderDto);
        return dishInOrderDtoList;
    }

    private DeliveryDetailsDto createDeliveryDetailsDto() {
        DeliveryDetailsDto deliveryDetailsDto = new DeliveryDetailsDto();
        deliveryDetailsDto.setFirstName("firstName");
        deliveryDetailsDto.setLastName("lastName");
        deliveryDetailsDto.setPhoneNumber("380-96-22-22-111");
        deliveryDetailsDto.setAddress("random address");
        return deliveryDetailsDto;
    }

    private CreateNewOrderDto createNewOrderDto() {
        List<DishInOrderDto> dishInOrderDtoList = getDishInOrderDto();

        return new CreateNewOrderDto(
                "afee6B5d-6f81-4B19-Ce9b-9FD5Bcf9B92d",
                dishInOrderDtoList,
                "comment",
                DeliveryMethod.ADDRESS_DELIVERY,
                createDeliveryDetailsDto()
        );
    }


    @Test
    void shouldCreateOrder() throws Exception {
        CreateNewOrderDto createNewOrderDto = createNewOrderDto();
        UUID expectedResponseBody = UUID.randomUUID();

        when(orderService.createOrder(any())).thenReturn(expectedResponseBody);

        mockMvc.perform(MockMvcRequestBuilders.post("/order-api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNewOrderDto)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(expectedResponseBody.toString()));
    }

    @Test
    void shouldThrow400BadRequestWhenRequestDtoNotValid() throws Exception {
        CreateNewOrderDto createNewOrderDto = createNewOrderDto();
        createNewOrderDto.setUserUuid("111");

        mockMvc.perform(MockMvcRequestBuilders.post("/order-api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNewOrderDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userUuid").value("Invalid uuid"));
    }

    @Test
    void shouldGetValidInputThenMapsToBusinessModel() throws Exception {
        CreateNewOrderDto createNewOrderDto = createNewOrderDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/order-api/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createNewOrderDto)))
                .andExpect(status().isCreated());
        ArgumentCaptor<CreateNewOrderDto> argumentCaptor = ArgumentCaptor.forClass(CreateNewOrderDto.class);

        verify(orderService, times(1)).createOrder(argumentCaptor.capture());
        CreateNewOrderDto orderCaptorValue = argumentCaptor.getValue();
        assertEquals(createNewOrderDto.getUserUuid(), orderCaptorValue.getUserUuid());
        assertEquals(createNewOrderDto.getComment(), orderCaptorValue.getComment());
        assertEquals(createNewOrderDto.getDeliveryMethod(), orderCaptorValue.getDeliveryMethod());
        assertEquals(createNewOrderDto.getDeliveryDetails(), orderCaptorValue.getDeliveryDetails());
        assertEquals(createNewOrderDto.getDishes(), orderCaptorValue.getDishes());
    }
}
