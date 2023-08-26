package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "dish_order")
public class DishInOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @OneToMany
    @JoinColumn(name = "dish_id", nullable = false, referencedColumnName = "id")
    private List<Dish> dishes;

    @OneToMany
    @JoinColumn(name = "order_id", nullable = false, referencedColumnName = "id")
    private List<Order> orders;

    @Column(name = "count", nullable = false)
    private Short count;

    @Column(name = "total_price_dish", nullable = false)
    private Double TotalPriceDish;
}