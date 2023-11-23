package com.crmfoodestablishment.usermanager.crud.service;

import com.crmfoodestablishment.usermanager.crud.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.RegisterUserResponseDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterUserResponseDTO register(RegisterUserRequestDTO creationData);

    void update(UUID userUuid, UpdateUserRequestDTO updatedData);

    void delete(UUID userUuid);

    UserDTO getById(UUID userUuid);

    List<UserDTO> listAll();
}
