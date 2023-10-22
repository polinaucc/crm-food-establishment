package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.exception.NotFoundException;
import com.crmfoodestablishment.coreservice.service.impl.OrderServiceConsumerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class OrderServiceConsumerImplTest {

    private OrderServiceConsumerImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private OrderMapper orderMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceConsumerImpl(orderRepository, dishRepository, orderMapper);
    }

    @Test
    public void testOrderCreateMethod() {
        NewOrderDto newOrderDto = new NewOrderDto();

        when(dishRepository.findByUuid(any(UUID.class)).orElseThrow(() -> new NotFoundException("Dish is not found")));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        UUID result = orderService.createOrder(newOrderDto);
        assertNotNull(result);

        verify(dishRepository, times(1)).findByUuid(any(UUID.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
