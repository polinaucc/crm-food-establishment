package com.crmfoodestablishment.user_auth_service.user_manager.services;

import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserCreationRequestPayload;

//TODO need to be completed and implemented by Andriy
public interface UserService {

    User findByEmail(String email);

    Boolean existsByEmail(String email);

    User createUser(UserCreationRequestPayload creationData);
}
