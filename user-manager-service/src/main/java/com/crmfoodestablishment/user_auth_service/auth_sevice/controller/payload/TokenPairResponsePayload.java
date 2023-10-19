package com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class TokenPairResponsePayload {

    @Pattern(regexp = "/^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)/")
    @NotBlank
    private String accessToken;

    @Pattern(regexp = "/^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)/")
    @NotBlank
    private String refreshToken;
}
