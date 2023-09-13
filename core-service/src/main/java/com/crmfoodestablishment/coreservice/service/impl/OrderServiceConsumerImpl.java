package com.crmfoodestablishment.coreservice.service.impl;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishInOrderRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceConsumerImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishInOrderRepository dishInOrderRepository;
    private final ModelMapper modelMapper;

    @Override
    public UUID createOrder(NewOrderDto newOrderDto) {
        TypeMap<NewOrderDto, Order> propertyMapper = modelMapper.createTypeMap(NewOrderDto.class, Order.class);
        propertyMapper.addMappings(
                modelMapper -> modelMapper.map(userUuid -> newOrderDto.getDishInOrderList().get(0).getOrder().getUserUuid(),
                        Order::setUserUuid));
        Order order = modelMapper.map(newOrderDto, Order.class);
        orderRepository.save(order);
        return order.getUuid();
    }

    @Override
    public void createDishInOrder(NewOrderDto newOrderDto) {
        dishInOrderRepository.saveAll(newOrderDto.getDishInOrderList());
    }
}