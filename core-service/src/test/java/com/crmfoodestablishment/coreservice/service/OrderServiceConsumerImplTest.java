package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.impl.OrderServiceConsumerImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

public class OrderServiceConsumerImplTest {

    private OrderServiceConsumerImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        orderService = new OrderServiceConsumerImpl(orderRepository, dishRepository, modelMapper);
    }

    @Test
    public void testOrderCreateMethod() {
        NewOrderDto newOrderDto = new NewOrderDto();
        List<DishInOrder> orderDishes = new ArrayList<>();
        newOrderDto.setListOfOrderDishes(orderDishes);

        when(dishRepository.findDishesByIdIn(anyList())).thenReturn(new ArrayList<>());
        when(modelMapper.map(newOrderDto, Order.class)).thenReturn(new Order());
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        UUID result = orderService.createOrder(newOrderDto);
        assertNotNull(result);

        verify(dishRepository, times(1)).findDishesByIdIn(anyList());
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
