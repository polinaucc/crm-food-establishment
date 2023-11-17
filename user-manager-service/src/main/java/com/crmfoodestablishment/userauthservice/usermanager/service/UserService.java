package com.crmfoodestablishment.userauthservice.usermanager.service;

import com.crmfoodestablishment.userauthservice.usermanager.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.RegisterUserResponseDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterUserResponseDTO register(RegisterUserRequestDTO creationData);

    void update(UUID userUuid, UpdateUserRequestDTO updatedData);

    void delete(UUID userUuid);

    UserDTO getById(UUID userUuid);

    List<UserDTO> listAll();
}
