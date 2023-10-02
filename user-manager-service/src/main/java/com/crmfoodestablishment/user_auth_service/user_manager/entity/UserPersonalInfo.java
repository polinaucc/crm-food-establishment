package com.crmfoodestablishment.user_auth_service.user_manager.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "personal_info")
@Getter
@Setter
@RequiredArgsConstructor
public class UserPersonalInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "firstName", length = 32)
    private String firstName;

    @Column(name = "lastName", length = 32)
    private String lastName;

    @Column(name = "isMale")
    private Boolean isMale;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Column(name = "address", length = 1024)
    private String address;
}
