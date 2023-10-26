package com.crmfoodestablishment.user_auth_service.auth_service.controller.payload;

import lombok.Getter;
import lombok.Setter;

//TODO better to be implemented by Andriy
//TODO write validation
@Getter
@Setter
public class UserRegistrationRequestPayload {

    private String email;
}
