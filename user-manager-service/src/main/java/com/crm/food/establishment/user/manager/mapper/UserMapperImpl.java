package com.crm.food.establishment.user.manager.mapper;

import com.crm.food.establishment.user.manager.dto.RegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UpdateUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.entity.UserPersonalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDTO mapUserToUserDTO(User user) {
        return new UserDTO(
                user.getUuid(),
                user.getEmail(),
                user.getRole(),
                user.getPersonalInfo().getFirstName(),
                user.getPersonalInfo().getLastName(),
                user.getPersonalInfo().isMale(),
                user.getPersonalInfo().getBirthday(),
                user.getPersonalInfo().getAddress()
        );
    }

    @Override
    public void mapRegisterUserRequestDTOToUser(
            RegisterUserRequestDTO dto,
            User user
    ) {
        user.setEmail(dto.getEmail());
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        user.setRole(dto.getRole());
        user.setPersonalInfo(new UserPersonalInfo());
        user.getPersonalInfo().setFirstName(dto.getFirstName());
        user.getPersonalInfo().setLastName(dto.getLastName());
        user.getPersonalInfo().setBirthday(dto.getBirthday());
        user.getPersonalInfo().setMale(dto.getIsMale());
        user.getPersonalInfo().setAddress(dto.getAddress());
    }

    @Override
    public void mapUpdateUserRequestDTOToUser(
            UpdateUserRequestDTO dto,
            User user
    ) {
        user.setEmail(dto.getEmail());
        user.setPassword(
                passwordEncoder.encode(dto.getPassword())
        );
        user.setRole(dto.getRole());
        user.setPersonalInfo(new UserPersonalInfo());
        user.getPersonalInfo().setFirstName(dto.getFirstName());
        user.getPersonalInfo().setLastName(dto.getLastName());
        user.getPersonalInfo().setBirthday(dto.getBirthday());
        user.getPersonalInfo().setMale(dto.getIsMale());
        user.getPersonalInfo().setAddress(dto.getAddress());
    }
}
