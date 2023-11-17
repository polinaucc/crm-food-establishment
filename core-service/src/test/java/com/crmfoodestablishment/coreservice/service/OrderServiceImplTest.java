package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.order.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.exception.NotFoundException;
import com.crmfoodestablishment.coreservice.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private DishRepository dishRepository;

    @Mock
    private OrderMapper orderMapper;

    @Test
    public void testOrderCreateMethod() {
        CreateNewOrderDto createNewOrderDto = new CreateNewOrderDto();

        when(dishRepository.findByUuid(any(UUID.class)).orElseThrow(() -> new NotFoundException("Dish is not found")));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        UUID result = orderService.createOrder(createNewOrderDto);
        assertNotNull(result);

        verify(dishRepository, times(1)).findByUuid(any(UUID.class));
        verify(orderRepository, times(1)).save(any(Order.class));
    }
}
