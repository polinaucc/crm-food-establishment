package com.crmfoodestablishment.usermanager.crud.dto;

import com.crmfoodestablishment.usermanager.auth.token.TokenPair;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
public class RegisterUserResponseDTO {

    private UUID userUuid;

    private TokenPair tokenPair;
}
