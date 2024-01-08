package com.crm.food.establishment.core.entity;

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
import java.util.Objects;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
//@EqualsAndHashCode // TODO: it may be lombok one, but it is required to exclude some fields
public class Dish {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    //TODO: add in DB migration
    @Column(name = "uuid")
    private UUID uuid;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(name, dish.name) && Objects.equals(price, dish.price) && Objects.equals(ingredients, dish.ingredients) && Objects.equals(menu, dish.menu);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price, ingredients, menu);
    }
}