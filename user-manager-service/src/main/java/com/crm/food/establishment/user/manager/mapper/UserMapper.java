package com.crm.food.establishment.user.manager.mapper;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.UserDto;
import com.crm.food.establishment.user.manager.entity.User;

import java.util.UUID;

public interface UserMapper {

    UserDto mapUserToUserDto(User user);

    User mapUpdateRegisterUserRequestDtoToUser(Long id, UUID uuid, UpdateRegisterUserRequestDto dto);
}
