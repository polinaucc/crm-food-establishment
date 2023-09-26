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
    @Column(name = "id", nullable = false, unique = true)
    private DishOrderId id = new DishOrderId();

    @Column(name = "count", nullable = false)
    private Short count;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("dishId")
    @JoinColumn(name = "dish_id")
    private Dish dish;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("orderId")
    @JoinColumn(name = "order_id")
    private Order order;
}