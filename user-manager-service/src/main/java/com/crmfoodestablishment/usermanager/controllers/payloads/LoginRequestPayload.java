package com.crmfoodestablishment.usermanager.controllers.payloads;

import lombok.Data;

@Data
public class LoginRequestPayload {

    private String email;

    private String password;
}
