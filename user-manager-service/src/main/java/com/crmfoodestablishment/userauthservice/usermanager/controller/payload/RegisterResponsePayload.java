package com.crmfoodestablishment.userauthservice.usermanager.controller.payload;

import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class RegisterResponsePayload {

    private String urlOfCreatedUser;

    private TokenPair tokenPair;
}
