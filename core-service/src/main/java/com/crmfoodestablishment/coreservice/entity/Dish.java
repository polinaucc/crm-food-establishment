package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "price", precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "ingredients", nullable = false, length = 512)
    private String ingredients;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id")
    private Menu menu;

    @OneToMany(
            mappedBy = "dish",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<DishInOrder> dishes;
}