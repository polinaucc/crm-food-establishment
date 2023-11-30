package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.dto.DeliveryDetailsDto;
import com.crmfoodestablishment.coreservice.dto.DishInOrderDto;
import com.crmfoodestablishment.coreservice.entity.DeliveryDetails;
import com.crmfoodestablishment.coreservice.entity.Dish;
import com.crmfoodestablishment.coreservice.entity.Order;
import com.crmfoodestablishment.coreservice.entity.DeliveryMethod;
import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import com.crmfoodestablishment.coreservice.mapper.OrderMapper;
import com.crmfoodestablishment.coreservice.repository.DishRepository;
import com.crmfoodestablishment.coreservice.repository.OrderRepository;
import com.crmfoodestablishment.coreservice.service.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;

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

    private Dish createDish() {
        Dish dish = new Dish();
        dish.setUuid(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        dish.setPrice(BigDecimal.ONE);
        return dish;
    }

    private List<DishInOrderDto> getDishInOrderDto() {
        DishInOrderDto dishInOrderDto = new DishInOrderDto();
        dishInOrderDto.setUuid(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
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

    private DeliveryDetails createDeliveryDetails() {
        DeliveryDetails deliveryDetails = new DeliveryDetails();
        deliveryDetails.setFirstName("firstName");
        deliveryDetails.setLastName("lastName");
        deliveryDetails.setPhoneNumber("380-96-22-22-111");
        deliveryDetails.setAddress("random address");
        return deliveryDetails;
    }

    private List<DishInOrder> createDishInOrder() {
        DishInOrder dishInOrder = new DishInOrder();
        dishInOrder.setUuid(UUID.fromString("3fa85f64-5717-4562-b3fc-2c963f66afa6"));
        dishInOrder.setAmount((short) 2);
        dishInOrder.setDish(createDish());

        List<DishInOrder> dishInOrders = new ArrayList<>();
        dishInOrders.add(dishInOrder);
        return dishInOrders;
    }

    private Order createOrder() {
        Order order = new Order();
        order.setUuid(UUID.randomUUID());
        order.setUserUuid(UUID.fromString("afee6B5d-6f81-4B19-Ce9b-9FD5Bcf9B92d"));
        order.setDishes(createDishInOrder());
        order.setDeliveryDetails(createDeliveryDetails());
        order.setDeliveryMethod(DeliveryMethod.ADDRESS_DELIVERY);
        order.setComment("comment");
        return order;
    }

    @Test
    void shouldCreateOrder() {
        CreateNewOrderDto createNewOrderDto = createNewOrderDto();
        Dish dish = createDish();
        Order order = createOrder();

        when(dishRepository.findByUuid(dish.getUuid())).thenReturn(Optional.of(dish));
        when(orderMapper.mapOrderDtoToOrder(any())).thenReturn(order);
        when(orderRepository.save(any())).thenReturn(order);
        UUID uuid = orderService.createOrder(createNewOrderDto);
        ArgumentCaptor<Order> argumentCaptor = ArgumentCaptor.forClass(Order.class);

        assertNotNull(uuid);
        verify(orderRepository, times(1)).save(argumentCaptor.capture());
        verify(orderMapper, times(1)).mapOrderDtoToOrder(createNewOrderDto);
        Order orderCaptorValue = argumentCaptor.getValue();
        assertNotNull(orderCaptorValue.getModificationDate());
        assertEquals(order.getTotalPrice(), orderCaptorValue.getTotalPrice());
        assertEquals(order.getUserUuid(), orderCaptorValue.getUserUuid());
        assertEquals(order.getComment(), orderCaptorValue.getComment());
        assertEquals(order.getDeliveryDetails(), orderCaptorValue.getDeliveryDetails());
        assertEquals(order.getDishes(), orderCaptorValue.getDishes());
    }

    @Test
    void shouldGetExceptionWhenDishUuidNotExist() {
        CreateNewOrderDto createNewOrderDto = createNewOrderDto();

        when(dishRepository.findByUuid(any(UUID.class)))
                .thenAnswer(invocation -> {
                    UUID argument = invocation.getArgument(0);
                    return Optional.empty();
                });

        assertThatThrownBy(() -> orderService.createOrder(createNewOrderDto))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Dish is not found");
    }
}
