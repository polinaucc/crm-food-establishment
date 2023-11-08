package com.crmfoodestablishment.userauthservice.usermanager.mapper;

import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        return UserDTO.builder()
                .uuid(user.getUuid())
                .email(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRoles())
                .personalInfo(user.getPersonalInfo())
                .build();
    }
}
