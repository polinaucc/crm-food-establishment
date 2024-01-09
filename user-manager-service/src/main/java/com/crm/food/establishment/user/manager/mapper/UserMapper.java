package com.crm.food.establishment.user.manager.mapper;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
import com.crm.food.establishment.user.manager.entity.User;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);

    void mapUpdateRegisterUserRequestDTOToUser(UpdateRegisterUserRequestDTO dto, User user);
}
