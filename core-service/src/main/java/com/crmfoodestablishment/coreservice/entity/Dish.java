package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.FetchType;
import jakarta.persistence.CascadeType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @JdbcTypeCode(SqlTypes.UUID)
    @Column(name = "uuid", nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "price", precision = 7, scale = 2, nullable = false)
    private BigDecimal price;

    @Column(name = "ingredients", nullable = false, length = 512)
    private String ingredients;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id")
    private Menu menu;

    @OneToMany(
            mappedBy = "dish",
            fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST,
            orphanRemoval = true
    )
    private List<DishInOrder> orders;

    public void setOrders(List<DishInOrder> orders) {
        this.orders = orders;
        this.orders.forEach(dishInOrder -> dishInOrder.setDish(this));
    }
}