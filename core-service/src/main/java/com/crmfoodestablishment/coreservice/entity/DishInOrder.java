package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dish_order")
public class DishInOrder {
    @EmbeddedId
    private DishOrderId dishOrderId;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID uuid;

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
