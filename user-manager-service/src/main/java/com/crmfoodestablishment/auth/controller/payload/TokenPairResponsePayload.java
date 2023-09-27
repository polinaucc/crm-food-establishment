package com.crmfoodestablishment.auth.controller.payload;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class TokenPairResponsePayload {

    private String accessToken;

    private String refreshToken;
}
