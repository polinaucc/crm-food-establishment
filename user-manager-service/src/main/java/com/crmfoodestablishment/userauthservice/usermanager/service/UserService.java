package com.crmfoodestablishment.userauthservice.usermanager.service;

import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.UpdateUserRequestPayload;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.UserRegistrationRequestPayload;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface UserService {

    UserDTO getByEmail(String email);

    Boolean existsByEmail(String email);

    RegisterResponsePayload register(UserRegistrationRequestPayload creationData, Role role);

    void update(UUID userUuid, UpdateUserRequestPayload updatedData);

    void updateRoles(UUID userUuid, Set<Role> updatedRoles);

    void delete(UUID userUuid);

    UserDTO getById(UUID userUuid);

    List<UserDTO> listAll();
}
