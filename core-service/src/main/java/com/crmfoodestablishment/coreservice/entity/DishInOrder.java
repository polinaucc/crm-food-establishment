package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

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
    Dish dish;

    @ManyToOne
    @MapsId("orderId")
    Order order;

    @Column(name = "total_price", precision = 7, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    public void sumOrder() {
        totalPrice = (BigDecimal) dish.getDishes().mapToDouble(dish -> dish.getDish().getPrice() * dish.count);
    }
}