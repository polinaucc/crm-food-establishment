package com.crm.food.establishment.user.manager.mapper;

import com.crm.food.establishment.user.manager.dto.RegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UpdateUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
import com.crm.food.establishment.user.manager.entity.User;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);

    void mapRegisterUserRequestDTOToUser(
            RegisterUserRequestDTO dto,
            User user
    );

    void mapUpdateUserRequestDTOToUser(
            UpdateUserRequestDTO dto,
            User user
    );
}
