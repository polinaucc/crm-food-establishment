package com.crmfoodestablishment.user_auth_service.auth_service.controller.payload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@Getter
@Setter
public class RegisterResponsePayload {

    @URL
    @NotBlank
    private String urlOfCreatedUser;

    @Valid
    private TokenPairResponsePayload tokenPair;
}
