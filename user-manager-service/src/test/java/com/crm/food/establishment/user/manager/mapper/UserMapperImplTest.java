package com.crm.food.establishment.user.manager.mapper;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.UserDto;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.entity.UserPersonalInfo;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class UserMapperImplTest {

    @Spy
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @InjectMocks
    private UserMapperImpl userMapper;

    @Test
    void mapUserToUserDTO() {
        UserPersonalInfo userPersonalInfo = new UserPersonalInfo();
        userPersonalInfo.setFirstName("John");
        userPersonalInfo.setLastName("Dou");
        userPersonalInfo.setMale(true);
        userPersonalInfo.setBirthday(LocalDate.now());
        userPersonalInfo.setAddress("Some address");
        User user = new User();
        user.setUuid(UUID.randomUUID());
        user.setEmail("test@gmail.com");
        user.setRole(Role.CLIENT);
        user.setPersonalInfo(userPersonalInfo);

        UserDto userDTO = userMapper.mapUserToUserDto(user);

        assertEquals(user.getUuid(), userDTO.uuid());
        assertEquals(user.getEmail(), userDTO.email());
        assertEquals(user.getRole(), userDTO.role());
        assertEquals(user.getPersonalInfo().getFirstName(), userDTO.firstName());
        assertEquals(user.getPersonalInfo().getLastName(), userDTO.lastName());
        assertEquals(user.getPersonalInfo().isMale(), userDTO.isMale());
        assertEquals(user.getPersonalInfo().getBirthday(), userDTO.birthday());
        assertEquals(user.getPersonalInfo().getAddress(), userDTO.address());
    }

    @Test
    void mapRegisterUserRequestDTOToUser() {
        UpdateRegisterUserRequestDto requestDTO = new UpdateRegisterUserRequestDto(
                "test@gmail.com",
                "qwerty1234",
                Role.CLIENT,
                "John",
                "Dou",
                true,
                LocalDate.now(),
                "Some address"
        );
        UUID expectedUuid = UUID.randomUUID();
        User user = userMapper.mapUpdateRegisterUserRequestDtoToUser(
                123L, expectedUuid, requestDTO
        );

        assertEquals(123L, user.getId());
        assertEquals(expectedUuid, user.getUuid());
        assertEquals(requestDTO.email(), user.getEmail());
        assertTrue(passwordEncoder.matches(requestDTO.password(), user.getPassword()));
        assertEquals(requestDTO.role(), user.getRole());
        assertEquals(requestDTO.firstName(), user.getPersonalInfo().getFirstName());
        assertEquals(requestDTO.lastName(), user.getPersonalInfo().getLastName());
        assertEquals(requestDTO.isMale(), user.getPersonalInfo().isMale());
        assertEquals(requestDTO.birthday(), user.getPersonalInfo().getBirthday());
        assertEquals(requestDTO.address(), user.getPersonalInfo().getAddress());
    }
}