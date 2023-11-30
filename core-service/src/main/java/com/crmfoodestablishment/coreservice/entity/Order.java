package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "client_order")
public class Order {

    @PrePersist
    public void initTotalPrice() {
        totalPrice = dishes.stream()
                .map(dishInOrder -> dishInOrder.getDish().getPrice().multiply(BigDecimal.valueOf(dishInOrder.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @PreUpdate
    public void changeDate() {
        this.modificationDate = LocalDateTime.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid = UUID.randomUUID();

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "comment", length = 128)
    private String comment;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE},
            orphanRemoval = true
    )
    private List<DishInOrder> dishes = new ArrayList<>();

    @OneToOne(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private DeliveryDetails deliveryDetails;

    @Column(name = "total_price", precision = 7, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", nullable = false, length = 16)
    private DeliveryMethod deliveryMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false, length = 16)
    private OrderStatus orderStatus = OrderStatus.NEW;

    @Column(name = "modification_date", nullable = false)
    private LocalDateTime modificationDate = LocalDateTime.now();

    public void setDishes(List<DishInOrder> listOfOrderDishes) {
        this.dishes = listOfOrderDishes;
        this.dishes.stream().forEach(dishInOrder -> dishInOrder.setOrder(this));
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
        this.deliveryDetails.setOrder(this);
    }

    public void addDish(Dish dish, Short amount) {
        DishInOrder dishInOrder = new DishInOrder(this, dish, amount);
        if (dish.getOrders() == null) {
            dish.setOrders(new ArrayList<>());
        }
        dishes.add(dishInOrder);
        dish.getOrders().add(dishInOrder);
    }
}
