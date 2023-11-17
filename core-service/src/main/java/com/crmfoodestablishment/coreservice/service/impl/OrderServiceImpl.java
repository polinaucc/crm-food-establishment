package com.crmfoodestablishment.coreservice.service.impl;

import com.crmfoodestablishment.coreservice.dto.order.CreateNewOrderDto;
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

import static com.crmfoodestablishment.coreservice.entity.DeliveryMethod.*;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final OrderMapper orderMapper;

    @Override
    @Transactional
    public UUID createOrder(CreateNewOrderDto createNewOrderDto) {
        validateDeliveryMethod(createNewOrderDto);

        Order orderToSave = orderMapper.mapOrderDtoToOrder(createNewOrderDto);
        createNewOrderDto.getDishes().forEach(dishWithAmount -> {
            Dish dish = dishRepository.findByUuid(dishWithAmount.getUuid()).orElseThrow(() -> new NotFoundException("Dish is not found"));
            orderToSave.addDish(dish, dishWithAmount.getAmount());
        });

        return orderRepository.save(orderToSave).getUuid();
    }

    private void validateDeliveryMethod(CreateNewOrderDto createNewOrderDto) {
        if (createNewOrderDto.getDeliveryDetails().getPhoneNumber() == null) {
            createNewOrderDto.setDeliveryMethod(LOCAL);
        } else if (createNewOrderDto.getDeliveryDetails().getAddress() == null) {
            createNewOrderDto.setDeliveryMethod(SELF_PICKUP);
        } else createNewOrderDto.setDeliveryMethod(ADDRESS_DELIVERY);
    }
}