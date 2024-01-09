package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterUserResponseDTO register(UpdateRegisterUserRequestDTO creationDTO);

    void update(UUID userUuid, UpdateRegisterUserRequestDTO updateDTO);

    void delete(UUID userUuid);

    UserDTO getById(UUID userUuid);

    List<UserDTO> listAll();
}
