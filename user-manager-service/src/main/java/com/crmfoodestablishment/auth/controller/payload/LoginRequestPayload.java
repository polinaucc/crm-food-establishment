package com.crmfoodestablishment.auth.controller.payload;

import lombok.Getter;

@Getter
public class LoginRequestPayload {

    private String email;

    private String password;
}
