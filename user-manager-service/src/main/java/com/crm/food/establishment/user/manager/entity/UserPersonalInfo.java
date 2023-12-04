package com.crm.food.establishment.user.manager.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class UserPersonalInfo {

    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @Column(name = "is_male", nullable = false)
    private boolean isMale;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "address", length = 1024)
    private String address;
}
