package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.manager.dto.RegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UpdateUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterUserResponseDTO register(RegisterUserRequestDTO creationData);

    void update(UUID userUuid, UpdateUserRequestDTO updatedData);

    void delete(UUID userUuid);

    UserDTO getById(UUID userUuid);

    List<UserDTO> listAll();
}
