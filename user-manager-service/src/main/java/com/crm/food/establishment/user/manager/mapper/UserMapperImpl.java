package com.crm.food.establishment.user.manager.mapper;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.UserDto;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.entity.UserPersonalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserMapperImpl implements UserMapper {

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDto mapUserToUserDto(User user) {
        return new UserDto(
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
    public User mapUpdateRegisterUserRequestDtoToUser(
            Long id, UUID uuid, UpdateRegisterUserRequestDto dto
    ) {
        User user = new User(uuid);
        user.setId(id);
        user.setEmail(dto.email());
        user.setPassword(
                passwordEncoder.encode(dto.password())
        );
        user.setRole(dto.role());
        user.setPersonalInfo(new UserPersonalInfo());
        user.getPersonalInfo().setFirstName(dto.firstName());
        user.getPersonalInfo().setLastName(dto.lastName());
        user.getPersonalInfo().setBirthday(dto.birthday());
        user.getPersonalInfo().setMale(dto.isMale());
        user.getPersonalInfo().setAddress(dto.address());

        return user;
    }
}
