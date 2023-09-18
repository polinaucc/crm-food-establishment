package com.crmfoodestablishment.usermanager.services;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.usermanager.controllers.payloads.UserCreationRequestPayload;

public interface UserService {

    User findByEmail(String email);

    User createUser(UserCreationRequestPayload creationData);
}
