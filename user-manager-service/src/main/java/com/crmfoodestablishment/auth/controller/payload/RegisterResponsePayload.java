package com.crmfoodestablishment.auth.controller.payload;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RegisterResponsePayload {

    private String urlOfCreatedUser;

    private TokenPairResponsePayload tokenPair;
}
