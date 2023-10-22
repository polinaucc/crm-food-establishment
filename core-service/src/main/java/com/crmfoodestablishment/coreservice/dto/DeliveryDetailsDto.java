package com.crmfoodestablishment.coreservice.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeliveryDetailsDto {
    private String address;
    private String phoneNumber;
    private String firstName;
    private String lastName;//TODO: firstName + lastName
}
