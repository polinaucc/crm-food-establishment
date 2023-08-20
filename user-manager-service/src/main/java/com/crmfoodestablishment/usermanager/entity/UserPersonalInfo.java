package com.crmfoodestablishment.usermanager.entity;

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

    private String firstName;
    private String lastName;
    private Boolean isMale;
    private LocalDate birthday;
    private String address;

}
