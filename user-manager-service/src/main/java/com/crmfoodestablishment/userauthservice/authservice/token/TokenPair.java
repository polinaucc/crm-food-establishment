package com.crmfoodestablishment.userauthservice.authservice.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenPair {

    private String accessToken;

    private String refreshToken;
}
