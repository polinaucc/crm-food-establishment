package com.crmfoodestablishment.coreservice.service.impl;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.OrderService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderServiceConsumerImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final DishRepository dishRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public UUID createOrder(NewOrderDto newOrderDto) {
        List<Integer> listDishesId = newOrderDto.getListOfOrderDishes().stream()
                .map(dishInOrder -> dishInOrder.getDish().getId())
                .toList();
        List<Dish> dishList = dishRepository.findDishesByIdIn(listDishesId);

        List<DishInOrder> resultDishInOrderList = new ArrayList<>();
        for (DishInOrder dishInOrder : newOrderDto.getListOfOrderDishes()) {
            Dish dish = dishList.stream()
                    .filter(d -> d.getId().equals(dishInOrder.getDish().getId()))
                    .findFirst()
                    .orElse(null);
            if (dish != null) {
                DishInOrder newDishInOrder = new DishInOrder();
                newDishInOrder.setDish(dish);
                newDishInOrder.setCount(dishInOrder.getCount());
                resultDishInOrderList.add(newDishInOrder);
            }
        }

        newOrderDto.setListOfOrderDishes(resultDishInOrderList);
        Order order = modelMapper.map(newOrderDto, Order.class);
        orderRepository.save(order);
        return order.getUuid();
    }

}