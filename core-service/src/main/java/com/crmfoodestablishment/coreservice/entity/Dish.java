package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.JoinColumn;
import lombok.*;
import jakarta.persistence.CascadeType;
import java.math.BigDecimal;
import jakarta.persistence.GenerationType;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "name", unique = true, nullable = false, length = 64)
    private String name;

    @Column(name = "price", precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "ingredients", nullable = false, length = 512)
    private String ingredients;

    @ManyToOne
    @JoinColumn(name = "menu_id",
            referencedColumnName = "id")
    private Menu menu;

    @OneToMany(
            mappedBy = "dish",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    public List<DishInOrder> dishes;
}