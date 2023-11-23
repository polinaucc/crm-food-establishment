package com.crmfoodestablishment.usermanager.crud.mapper;

import com.crmfoodestablishment.usermanager.crud.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UserDTO;
import com.crmfoodestablishment.usermanager.crud.entity.Role;
import com.crmfoodestablishment.usermanager.crud.entity.User;
import com.crmfoodestablishment.usermanager.crud.entity.UserPersonalInfo;
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

        UserDTO userDTO = userMapper.mapUserToUserDTO(user);

        assertEquals(user.getUuid(), userDTO.getUuid());
        assertEquals(user.getEmail(), userDTO.getEmail());
        assertEquals(user.getRole(), userDTO.getRole());
        assertEquals(user.getPersonalInfo().getFirstName(), userDTO.getFirstName());
        assertEquals(user.getPersonalInfo().getLastName(), userDTO.getLastName());
        assertEquals(user.getPersonalInfo().isMale(), userDTO.isMale());
        assertEquals(user.getPersonalInfo().getBirthday(), userDTO.getBirthday());
        assertEquals(user.getPersonalInfo().getAddress(), userDTO.getAddress());
    }

    @Test
    void mapRegisterUserRequestDTOToUser() {
        RegisterUserRequestDTO requestDTO = new RegisterUserRequestDTO();
        requestDTO.setEmail("test@gmail.com");
        requestDTO.setPassword("qwerty");
        requestDTO.setRole(Role.CLIENT);
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Dou");
        requestDTO.setIsMale(true);
        requestDTO.setBirthday(LocalDate.now());
        requestDTO.setAddress("Some address");

        User user = new User();

        userMapper.mapRegisterUserRequestDTOToUser(
                requestDTO,
                user
        );

        assertEquals(requestDTO.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(
                requestDTO.getPassword(),
                user.getPassword()
        ));
        assertEquals(requestDTO.getRole(), user.getRole());
        assertEquals(requestDTO.getFirstName(), user.getPersonalInfo().getFirstName());
        assertEquals(requestDTO.getLastName(), user.getPersonalInfo().getLastName());
        assertEquals(requestDTO.getIsMale(), user.getPersonalInfo().isMale());
        assertEquals(requestDTO.getBirthday(), user.getPersonalInfo().getBirthday());
        assertEquals(requestDTO.getAddress(), user.getPersonalInfo().getAddress());
    }

    @Test
    void mapUpdateUserRequestDTOToUser() {
        UpdateUserRequestDTO requestDTO = new UpdateUserRequestDTO();
        requestDTO.setEmail("test@gmail.com");
        requestDTO.setPassword("qwerty");
        requestDTO.setRole(Role.CLIENT);
        requestDTO.setFirstName("John");
        requestDTO.setLastName("Dou");
        requestDTO.setIsMale(true);
        requestDTO.setBirthday(LocalDate.now());
        requestDTO.setAddress("Some address");

        User user = new User();

        userMapper.mapUpdateUserRequestDTOToUser(
                requestDTO,
                user
        );

        assertEquals(requestDTO.getEmail(), user.getEmail());
        assertTrue(passwordEncoder.matches(
                requestDTO.getPassword(),
                user.getPassword()
        ));
        assertEquals(requestDTO.getRole(), user.getRole());
        assertEquals(requestDTO.getFirstName(), user.getPersonalInfo().getFirstName());
        assertEquals(requestDTO.getLastName(), user.getPersonalInfo().getLastName());
        assertEquals(requestDTO.getIsMale(), user.getPersonalInfo().isMale());
        assertEquals(requestDTO.getBirthday(), user.getPersonalInfo().getBirthday());
        assertEquals(requestDTO.getAddress(), user.getPersonalInfo().getAddress());
    }
}