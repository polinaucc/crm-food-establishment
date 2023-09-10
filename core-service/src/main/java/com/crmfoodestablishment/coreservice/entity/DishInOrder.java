package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish_order")
public class DishInOrder {

    @EmbeddedId
    @Column(name = "id", nullable = false, unique = true)
    private DishOrderId id;

    @Column(name = "count", nullable = false)
    private Short count;

    @ManyToOne
    @MapsId("dishId")
    private Dish dish;

    @ManyToOne
    @MapsId("orderId")
    private Order order;
}