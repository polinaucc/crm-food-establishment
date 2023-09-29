package com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@AllArgsConstructor
@Getter
@Setter
public class RegisterResponsePayload {

    @URL
    private String urlOfCreatedUser;

    @Valid
    private TokenPairResponsePayload tokenPair;
}
