package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "menu")
    private List<Dish> dishes;
}
