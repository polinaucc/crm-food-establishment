package com.crm.food.establishment.user.auth.service;

import com.crm.food.establishment.user.auth.dto.CredentialsDTO;
import com.crm.food.establishment.user.auth.token.TokenPair;

import java.util.UUID;

public interface AuthService {

    TokenPair login(CredentialsDTO credentials);

    String refresh(String refreshToken);

    void logout(UUID userUuid);
}
