package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.mapper.OrderMapper;
import com.crmfoodestablishment.coreservice.service.exception.NotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.UUID;
import static com.crmfoodestablishment.coreservice.entity.DeliveryMethod.ADDRESS_DELIVERY;
import static com.crmfoodestablishment.coreservice.entity.DeliveryMethod.LOCAL;
import static com.crmfoodestablishment.coreservice.entity.DeliveryMethod.SELF_PICKUP;

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
            Dish dish = dishRepository.findByUuid(dishWithAmount.getUuid()).orElseThrow(()
                    -> new NotFoundException("Dish is not found"));
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