package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "dish_order")
public class DishOfOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToMany
    @JoinColumn(name = "dish_id", referencedColumnName = "id")
    private List<Dish> dishes;

    @OneToMany
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private List<Order> orders;

    private Short count;

    @Column(name = "total_price_dish")
    private Double TotalPriceDish;
}
