package com.crmfoodestablishment.usermanager.payloads;

import lombok.Data;

@Data
public class LoginRequestPayload {

    private String email;

    private String password;
}
