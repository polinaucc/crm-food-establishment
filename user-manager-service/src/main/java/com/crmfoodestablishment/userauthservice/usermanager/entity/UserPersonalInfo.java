package com.crmfoodestablishment.userauthservice.usermanager.entity;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

import jakarta.persistence.Column;

import java.time.LocalDate;

@Embeddable
@Getter
@Setter
public class UserPersonalInfo {

    @Column(name = "firstName", nullable = false, length = 32)
    private String firstName;

    @Column(name = "lastName", nullable = false, length = 32)
    private String lastName;

    @Column(name = "isMale", nullable = false)
    private Boolean isMale;

    @Column(name = "birthday", nullable = false)
    private LocalDate birthday;

    @Column(name = "address", length = 1024)
    private String address;
}
