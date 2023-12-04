package com.crm.food.establishment.user.manager.dto;

import com.crm.food.establishment.user.auth.token.TokenPair;
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
