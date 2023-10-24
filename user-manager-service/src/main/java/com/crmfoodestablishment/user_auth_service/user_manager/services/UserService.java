package com.crmfoodestablishment.user_auth_service.user_manager.services;

import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserRegistrationRequestPayload;

import java.util.UUID;

//TODO need to be completed and implemented by Andriy
public interface UserService {

    User findByEmail(String email);

    User findByUuid(UUID uuid);

    Boolean existsByEmail(String email);

    //TODO don't forget to hash password before saving user
    User createUser(UserRegistrationRequestPayload creationData);
}
