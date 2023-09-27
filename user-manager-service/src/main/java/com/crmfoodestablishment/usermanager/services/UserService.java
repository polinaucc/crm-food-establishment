package com.crmfoodestablishment.usermanager.services;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.auth.controller.payload.UserCreationRequestPayload;

//TODO need to be completed and implemented by Andriy
public interface UserService {

    User findByEmail(String email);

    User createUser(UserCreationRequestPayload creationData);
}
