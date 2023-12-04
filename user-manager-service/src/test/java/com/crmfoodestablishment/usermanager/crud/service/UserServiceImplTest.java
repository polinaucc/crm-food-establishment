package com.crmfoodestablishment.usermanager.crud.service;

import com.crmfoodestablishment.usermanager.auth.service.JwtService;
import com.crmfoodestablishment.usermanager.auth.token.TokenPair;
import com.crmfoodestablishment.usermanager.crud.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.RegisterUserResponseDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.usermanager.crud.dto.UserDTO;
import com.crmfoodestablishment.usermanager.crud.entity.Role;
import com.crmfoodestablishment.usermanager.crud.entity.User;
import com.crmfoodestablishment.usermanager.crud.entity.UserPersonalInfo;
import com.crmfoodestablishment.usermanager.crud.exception.InvalidArgumentException;
import com.crmfoodestablishment.usermanager.crud.exception.NotFoundException;
import com.crmfoodestablishment.usermanager.crud.mapper.UserMapper;
import com.crmfoodestablishment.usermanager.crud.mapper.UserMapperImpl;
import com.crmfoodestablishment.usermanager.crud.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Spy
    private UserMapper userMapper = new UserMapperImpl(
            new BCryptPasswordEncoder()
    );

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    private RegisterUserRequestDTO testRegisterUserRequestDTO;
    private User testUser;
    private UpdateUserRequestDTO testUpdateUserRequestDTO;

    @BeforeEach
    public void setUpTestData() {
        testRegisterUserRequestDTO = new RegisterUserRequestDTO();
        testRegisterUserRequestDTO.setEmail("test@gmail.com");
        testRegisterUserRequestDTO.setPassword("qwerty");
        testRegisterUserRequestDTO.setRole(Role.CLIENT);
        testRegisterUserRequestDTO.setFirstName("John");
        testRegisterUserRequestDTO.setLastName("Dou");
        testRegisterUserRequestDTO.setIsMale(true);
        testRegisterUserRequestDTO.setBirthday(LocalDate.now());
        testRegisterUserRequestDTO.setAddress("Some address");

        UserPersonalInfo userPersonalInfo = new UserPersonalInfo();
        userPersonalInfo.setFirstName(testRegisterUserRequestDTO.getFirstName());
        userPersonalInfo.setLastName(testRegisterUserRequestDTO.getLastName());
        userPersonalInfo.setMale(testRegisterUserRequestDTO.getIsMale());
        userPersonalInfo.setBirthday(testRegisterUserRequestDTO.getBirthday());
        userPersonalInfo.setAddress(testRegisterUserRequestDTO.getAddress());

        testUser = new User(UUID.randomUUID());
        testUser.setEmail(testRegisterUserRequestDTO.getEmail());
        testUser.setPassword(testRegisterUserRequestDTO.getPassword());
        testUser.setRole(testRegisterUserRequestDTO.getRole());
        testUser.setPersonalInfo(userPersonalInfo);

        testUpdateUserRequestDTO = new UpdateUserRequestDTO();
        testUpdateUserRequestDTO.setEmail(testRegisterUserRequestDTO.getEmail());
        testUpdateUserRequestDTO.setPassword(testRegisterUserRequestDTO.getPassword());
        testUpdateUserRequestDTO.setRole(testRegisterUserRequestDTO.getRole());
        testUpdateUserRequestDTO.setFirstName(testRegisterUserRequestDTO.getFirstName());
        testUpdateUserRequestDTO.setLastName(testRegisterUserRequestDTO.getLastName());
        testUpdateUserRequestDTO.setIsMale(testRegisterUserRequestDTO.getIsMale());
        testUpdateUserRequestDTO.setBirthday(testRegisterUserRequestDTO.getBirthday());
        testUpdateUserRequestDTO.setAddress(testRegisterUserRequestDTO.getAddress());
    }

    @Test
    public void register_RegistersClient_And_ReturnRegisterResponse() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(userRepository.save(any()))
                .thenReturn(testUser);
        when(jwtService.issueTokenPair(any()))
                .thenReturn(new TokenPair("", ""));

        RegisterUserResponseDTO responseDTO = userService.register(
                testRegisterUserRequestDTO
        );

        verify(userRepository, times(1)).save(any());
        assertEquals(testUser.getUuid(), responseDTO.getUserUuid());
        assertNotNull(responseDTO.getTokenPair());
    }

    @Test
    public void register_RegistersEmployeeFromExisting() {
        testRegisterUserRequestDTO.setRole(Role.EMPLOYEE);

        testUser.setRole(Role.CLIENT);
        testUser.getPersonalInfo().setFirstName("Jack");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(testUser));
        when(userRepository.save(any()))
                .thenReturn(testUser);
        when(jwtService.issueTokenPair(any()))
                .thenReturn(new TokenPair("", ""));

        RegisterUserResponseDTO responseDTO = userService.register(
                testRegisterUserRequestDTO
        );

        verify(userRepository, times(1)).save(any());
        assertEquals(testUser.getUuid(), responseDTO.getUserUuid());
        assertEquals(
                testRegisterUserRequestDTO.getRole(),
                testUser.getRole()
        );
        assertEquals(
                testRegisterUserRequestDTO.getFirstName(),
                testUser.getPersonalInfo().getFirstName()
        );
        assertNotNull(responseDTO.getTokenPair());
    }

    @Test
    public void register_ValidatesEmailUniqueness() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(testUser));

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.register(
                        testRegisterUserRequestDTO
                )
        );
    }

    @Test
    public void update_UpdatesUser() {
        testUpdateUserRequestDTO.setEmail("new@gmail.com");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(testUser));

        userService.update(testUser.getUuid(), testUpdateUserRequestDTO);

        verify(userRepository, times(1)).save(any());
        assertEquals(
                testUpdateUserRequestDTO.getEmail(),
                testUser.getEmail()
        );
    }

    @Test
    public void update_UpdatesUserExceptEmail() {
        testUpdateUserRequestDTO.setFirstName("Jack");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(testUser));

        userService.update(testUser.getUuid(), testUpdateUserRequestDTO);

        verify(userRepository, times(1)).save(any());
        assertEquals(
                testUpdateUserRequestDTO.getFirstName(),
                testUser.getPersonalInfo().getFirstName()
        );
    }

    @Test
    public void update_ValidatesEmailUniqueness() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(testUser));

        assertThrows(
                InvalidArgumentException.class,
                () -> userService.update(
                        UUID.randomUUID(),
                        testUpdateUserRequestDTO
                )
        );
    }

    @Test
    public void update_ThrowsNotFoundException() {
        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.update(
                        testUser.getUuid(),
                        testUpdateUserRequestDTO
                )
        );
    }

    @Test
    public void delete_FiresEmployee() {
        testUser.setRole(Role.ADMIN);

        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(testUser));

        userService.delete(testUser.getUuid());

        verify(userRepository, times(1)).save(any());
        assertEquals(Role.CLIENT, testUser.getRole());
    }

    @Test
    public void delete_DeletesClient() {
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(testUser));

        userService.delete(testUser.getUuid());

        verify(userRepository, times(1)).delete(any());
    }

    @Test
    public void delete_ThrowsNotFoundException() {
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.delete(testUser.getUuid())
        );
    }

    @Test
    public void getById_ReturnsUserDTO() {
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(testUser));

        UserDTO userDTO = userService.getById(testUser.getUuid());

        verify(userRepository, times(1)).findByUuid(any());
        assertEquals(testUser.getUuid(), userDTO.getUuid());
    }

    @Test
    public void getById_ThrowsNotFoundException() {
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                NotFoundException.class,
                () -> userService.getById(testUser.getUuid())
        );
    }

    @Test
    public void listAll_ReturnsListUserDTO() {
        when(userRepository.findAll())
                .thenReturn(
                        List.of(testUser, testUser)
                );

        List<UserDTO> dtoList = userService.listAll();

        assertEquals(2, dtoList.size());
    }
}