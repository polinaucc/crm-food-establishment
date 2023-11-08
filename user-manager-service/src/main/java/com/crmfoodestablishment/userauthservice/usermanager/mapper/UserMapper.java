package com.crmfoodestablishment.userauthservice.usermanager.mapper;

import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.entity.User;

public interface UserMapper {

    UserDTO mapUserToUserDTO(User user);
}
