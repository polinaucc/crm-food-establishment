package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Data
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