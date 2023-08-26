package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Dish {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "price", nullable = false)
    private Double price;

    @Column(name = "ingredients", nullable = false, length = 512)
    private String ingredients;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, referencedColumnName = "id")
    private Menu menu;
}