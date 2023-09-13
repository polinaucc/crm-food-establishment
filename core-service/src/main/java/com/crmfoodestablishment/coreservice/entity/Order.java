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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "uuid", nullable = false)
    private UUID uuid = UUID.randomUUID();

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(name = "user_uuid", nullable = false)
    private UUID userUuid;

    @Column(name = "creation_date", nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Column(name = "comment", length = 128)
    private String comment;

    @OneToMany(
            mappedBy = "order",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<DishInOrder> listOfOrderDishes = new ArrayList<>();

    @Column(name = "total_price", precision = 7, scale = 2, nullable = false)
    private BigDecimal totalPrice;

    @PrePersist
    public void sumOrder() {
        totalPrice = listOfOrderDishes.stream().map(DishInOrder -> DishInOrder.getDish().getPrice()
                .multiply(BigDecimal.valueOf(DishInOrder.getCount())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}