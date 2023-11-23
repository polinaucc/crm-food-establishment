package com.crmfoodestablishment.usermanager.crud.mapper;

import com.crmfoodestablishment.usermanager.crud.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UserDTO;
import com.crmfoodestablishment.usermanager.crud.entity.User;

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
