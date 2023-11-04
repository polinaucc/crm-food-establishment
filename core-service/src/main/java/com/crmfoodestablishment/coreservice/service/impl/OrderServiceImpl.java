package com.crmfoodestablishment.coreservice.service.impl;

import com.crmfoodestablishment.coreservice.dto.order.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.OrderMapper;
import com.crmfoodestablishment.coreservice.service.OrderService;
import com.crmfoodestablishment.coreservice.service.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public UUID createOrder(NewOrderDto newOrderDto) {

        newOrderDto.validateDeliveryMethod(newOrderDto.getDeliveryDetails());
        Order orderToSave = orderMapper.newOrderDtoToOrder(newOrderDto);
        newOrderDto.getDishes().forEach(dishWithAmount -> {
            Dish dish = dishRepository.findByUuid(dishWithAmount.getUuid()).orElseThrow(() -> new NotFoundException("Dish is not found"));
            orderToSave.addDish(dish, dishWithAmount.getAmount());
        });

        return orderRepository.save(orderToSave).getUuid();
    }
}