package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcType;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID userUuid;

    @Column(name = "creation_date")
    private LocalDateTime creationDate;

    private String comment;

    @Column(name = "total_price")
    private Double totalPrice;
}