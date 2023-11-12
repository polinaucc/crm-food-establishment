package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Embeddable
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class DishOrderId implements Serializable {

    @Column(name = "order_id", insertable = false, updatable = false)
    private Integer orderId;

    @Column(name = "dish_id", insertable = false, updatable = false)
    private Integer dishId;
}