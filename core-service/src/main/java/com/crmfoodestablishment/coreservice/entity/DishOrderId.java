package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DishOrderId implements Serializable {
    @Serial
    private static final long serialVersionUID = -7023219131140812647L;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "dish_id", nullable = false)
    private Long dishId;
}