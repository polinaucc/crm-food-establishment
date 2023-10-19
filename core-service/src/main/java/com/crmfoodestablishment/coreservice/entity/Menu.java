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
public class Menu {

    @Id
    @Column(name = "uuid", nullable = false, unique = true, columnDefinition = "BINARY(16)")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID uuid;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true, length = 64)
    private String name;

    @Column(name = "comment", length = 128)
    private String comment;

    @Enumerated(EnumType.STRING)
    @Column(name = "season", nullable = false, length = 32)
    private Season season;

    @OneToMany(mappedBy = "menu")
    private List<Dish> dishes;

    public Menu(String name, String comment, Season season) {
        this.name = name;
        this.comment = comment;
        this.season = season;
    }
}
