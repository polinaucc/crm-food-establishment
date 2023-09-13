package com.crmfoodestablishment.coreservice.entity;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderTest {

    private Order order;

    @Test
    public void checkPrePersistMethodInitOfTotalPrice() {
        Dish filedMignon = new Dish();
        filedMignon.setPrice(BigDecimal.valueOf(123.76));
        Dish salad = new Dish();
        salad.setPrice(BigDecimal.valueOf(666.203));

        DishInOrder dishInOrder1 = new DishInOrder();
        DishInOrder dishInOrder2 = new DishInOrder();

        List<DishInOrder> dishInOrderList = new ArrayList<>();

        dishInOrderList.add(dishInOrder1);
        dishInOrderList.add(dishInOrder2);

        dishInOrderList.get(0).setDish(filedMignon);
        dishInOrderList.get(0).setCount((short) 2);
        dishInOrderList.get(1).setDish(salad);
        dishInOrderList.get(1).setCount((short) 3);

        order = new Order();

        order.setListOfOrderDishes(dishInOrderList);
        Assertions.assertEquals(BigDecimal.valueOf(2246.129), getTotalPrice2());
    }

    private BigDecimal getTotalPrice2() {
        return order.getListOfOrderDishes().stream()
                .map(dishInOrder -> dishInOrder.getDish().getPrice().multiply(BigDecimal.valueOf(dishInOrder.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}