package com.crmfoodestablishment.coreservice.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToOne;
import jakarta.persistence.FetchType;
import jakarta.persistence.MapsId;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.UUID;

@Entity
@Table(name = "delivery_details")
@Getter
@Setter
@NoArgsConstructor
public class DeliveryDetails {

    @Id
    @Column(name = "order_id", insertable = false, updatable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "uuid", nullable = false)
    private UUID uuid = UUID.randomUUID();

    @Column(name = "first_name", nullable = false, length = 64)
    private String firstName;

    @Column(name = "last_name", length = 64)
    private String lastName;

    @Column(name = "phone_number", nullable = false, length = 20)
    private String phoneNumber;

    @Column(name = "address", nullable = false, length = 1024)
    private String address;

    public DeliveryDetails(Order order, UUID uuid, String firstName, String lastName, String phoneNumber, String address) {
        this.order = order;
        this.uuid = uuid;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.id = order.getId();
    }
}
