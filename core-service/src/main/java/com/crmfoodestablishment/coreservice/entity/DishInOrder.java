package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish_order")
public class DishInOrder {
    @EmbeddedId
    private DishOrderId dishOrderId;

    @Column(name = "amount", nullable = false)
    private Short amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;

    public DishInOrder(Order order, Dish dish, Short amount) {
        this.amount = amount;
        this.dish = dish;
        this.order = order;
        this.dishOrderId = new DishOrderId(order.getId(), dish.getId());
    }
}