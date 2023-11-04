package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.*;
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
@Table(name = "orders")
public class Order {

    @PrePersist
    public void initTotalPrice() {
        totalPrice = dishes.stream()
                .map(dishInOrder -> dishInOrder.getDish().getPrice().multiply(BigDecimal.valueOf(dishInOrder.getAmount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
    private DeliveryDetails deliveryDetails = new DeliveryDetails();

    @Column(name = "total_price", precision = 7, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @Enumerated(EnumType.STRING)
    @Column(name = "delivery_method", nullable = false, length = 16)
    private DeliveryMethod deliveryMethod;

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
        dishes.add(dishInOrder);
        dish.getOrders().add(dishInOrder);
    }
}