package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDto;
import com.crm.food.establishment.user.manager.dto.UserDto;

import java.util.List;
import java.util.UUID;

public interface UserService {

    RegisterUserResponseDto registerUser(UpdateRegisterUserRequestDto creationDto);

    void updateUser(UUID userUuid, UpdateRegisterUserRequestDto updateDto);

    void deleteUser(UUID userUuid);

    UserDto getUserById(UUID userUuid);

    List<UserDto> getAllUsers();
}
