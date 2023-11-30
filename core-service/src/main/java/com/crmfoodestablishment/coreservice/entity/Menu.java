package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.EnumType;
import lombok.Getter;
import lombok.Setter;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;
import java.util.UUID;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Menu {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Integer id;

    @Column(name = "uuid", nullable = false, unique = true, updatable = false)
    @JdbcTypeCode(SqlTypes.UUID)
    private UUID uuid;

    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    @Column(name = "comment", length = 128)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false, length = 32)
    private Season season;

    @OneToMany(mappedBy = "menu",
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            cascade = CascadeType.REMOVE
            )
    private List<Dish> dishes;

    public void addDish(Dish dish) {
        dishes.add(dish);
        dish.setMenu(this);
    }
    public void removeDish(Dish dish) {
        dishes.remove(dish);
        dish.setMenu(null);
    }
}
