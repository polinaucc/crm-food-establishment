package com.crmfoodestablishment.coreservice.web;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;
import com.crmfoodestablishment.coreservice.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

    @InjectMocks
    private OrderController orderController;

    @Mock
    private OrderService orderService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateOrder() {
        NewOrderDto newOrderDto = new NewOrderDto();
        UUID orderUuid = UUID.randomUUID();
        when(orderService.createOrder(newOrderDto)).thenReturn(orderUuid);
        ResponseEntity<UUID> response = orderController.createOrder(newOrderDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(orderUuid, response.getBody());
        verify(orderService, times(1)).createOrder(newOrderDto);
    }
}