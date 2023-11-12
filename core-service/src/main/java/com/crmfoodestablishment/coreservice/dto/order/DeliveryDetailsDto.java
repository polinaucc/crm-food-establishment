package com.crmfoodestablishment.coreservice.dto.order;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDetailsDto {

    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String firstName;

    @Pattern(regexp = "^[a-zA-Zа-яА-Я]+$")
    private String lastName;

    @Pattern(regexp = "\\d+")
    private String phoneNumber;

    private String address;
}
