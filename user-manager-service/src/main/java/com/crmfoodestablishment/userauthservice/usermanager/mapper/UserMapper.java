package com.crmfoodestablishment.userauthservice.usermanager.mapper;

import com.crmfoodestablishment.userauthservice.usermanager.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.entity.User;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);

    User mapRegisterUserRequestDTOToUser(
            RegisterUserRequestDTO dto,
            User user
    );

    User mapUpdateUserRequestDTOToUser(
            UpdateUserRequestDTO dto,
            User user
    );
}
