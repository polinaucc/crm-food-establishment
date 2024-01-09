package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.auth.service.AuthService;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.entity.UserPersonalInfo;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;
import com.crm.food.establishment.user.manager.mapper.UserMapper;
import com.crm.food.establishment.user.manager.mapper.UserMapperImpl;
import com.crm.food.establishment.user.manager.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Spy
    private UserMapper userMapper = new UserMapperImpl(passwordEncoder);

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private AuthService authService;

    @Captor
    ArgumentCaptor<User> userArgumentCaptor;

    @InjectMocks
    private UserServiceImpl userService;

    private UpdateRegisterUserRequestDTO updateRegisterPayloadSample;
    private User userSample;

    @BeforeEach
    public void setUp() {
        updateRegisterPayloadSample = new UpdateRegisterUserRequestDTO(
                "test@gmail.com",
                "qwerty1234",
                Role.CLIENT,
                "John",
                "Dou",
                true,
                LocalDate.now(),
                "Some address"
        );

        userSample = new User(UUID.randomUUID());
        userSample.setEmail(updateRegisterPayloadSample.email());
        userSample.setPassword(
                passwordEncoder.encode(updateRegisterPayloadSample.password())
        );
        userSample.setRole(updateRegisterPayloadSample.role());
        userSample.setPersonalInfo(new UserPersonalInfo());
        userSample.getPersonalInfo().setFirstName(updateRegisterPayloadSample.firstName());
        userSample.getPersonalInfo().setLastName(updateRegisterPayloadSample.lastName());
        userSample.getPersonalInfo().setBirthday(updateRegisterPayloadSample.birthday());
        userSample.getPersonalInfo().setMale(updateRegisterPayloadSample.isMale());
        userSample.getPersonalInfo().setAddress(updateRegisterPayloadSample.address());
    }

    @Test
    public void register_ShouldRegisterNewClient_And_ReturnRegisterResponse() {
        var expectedRegisterResponse = new RegisterUserResponseDTO(
                userSample.getUuid(),
                new TokenPair("", "")
        );
        when(userRepository.findByEmail(updateRegisterPayloadSample.email())).thenReturn(Optional.empty());
        when(userRepository.save(any())).thenReturn(userSample);
        when(jwtService.issueTokenPair(userSample)).thenReturn(expectedRegisterResponse.tokenPair());

        RegisterUserResponseDTO actualRegisterResponse = userService.register(updateRegisterPayloadSample);

        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(userArgumentCaptor.getValue().getEmail(), userSample.getEmail());
        verify(jwtService).issueTokenPair(userSample);
        assertEquals(expectedRegisterResponse, actualRegisterResponse);
    }

    @Test
    public void register_ShouldRegisterEmployeeFromExisting() {
        var employeeRegisterDTO = new UpdateRegisterUserRequestDTO(
                "test@gmail.com",
                "qwerty1234",
                Role.EMPLOYEE,
                "John",
                "Dou",
                true,
                LocalDate.now(),
                "Some address"
        );
        when(userRepository.findByEmail(employeeRegisterDTO.email())).thenReturn(Optional.of(userSample));
        when(userRepository.save(userSample)).thenReturn(userSample);
        when(jwtService.issueTokenPair(userSample)).thenReturn(new TokenPair("", ""));

        RegisterUserResponseDTO responseDTO = userService.register(employeeRegisterDTO);

        verify(userRepository).save(userSample);
        verify(jwtService).issueTokenPair(userSample);
        assertEquals(userSample.getUuid(), responseDTO.userUuid());
        assertEquals(employeeRegisterDTO.role(), userSample.getRole());
        assertNotNull(responseDTO.tokenPair());
    }

    @Test
    public void register_ShouldValidateEmailUniqueness() {
        when(userRepository.findByEmail(updateRegisterPayloadSample.email())).thenReturn(Optional.of(userSample));

        assertThatThrownBy(() -> userService.register(updateRegisterPayloadSample))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Given already occupied email");
        verify(userRepository).findByEmail(updateRegisterPayloadSample.email());
        verify(userRepository, never()).save(any());
        verifyNoInteractions(jwtService);
    }

    @Test
    public void update_ShouldUpdateUser() {
        userSample.setEmail("old@gmail.com");
        when(userRepository.findByEmail(updateRegisterPayloadSample.email())).thenReturn(Optional.empty());
        when(userRepository.findByUuid(userSample.getUuid())).thenReturn(Optional.of(userSample));

        userService.update(userSample.getUuid(), updateRegisterPayloadSample);

        verify(userRepository).findByEmail(updateRegisterPayloadSample.email());
        verify(userRepository).findByUuid(userSample.getUuid());
        verify(userMapper).mapUpdateRegisterUserRequestDTOToUser(updateRegisterPayloadSample, userSample);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(userSample.getUuid(), userArgumentCaptor.getValue().getUuid());
        assertEquals(updateRegisterPayloadSample.email(), userArgumentCaptor.getValue().getEmail());
    }

    @Test
    public void update_ShouldAcceptUpdateOfSameUser() {
        userSample.getPersonalInfo().setFirstName("Old_Name");
        when(userRepository.findByEmail(updateRegisterPayloadSample.email())).thenReturn(Optional.of(userSample));

        userService.update(userSample.getUuid(), updateRegisterPayloadSample);

        verify(userRepository).findByEmail(updateRegisterPayloadSample.email());
        verify(userMapper).mapUpdateRegisterUserRequestDTOToUser(updateRegisterPayloadSample, userSample);
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(userSample.getUuid(), userArgumentCaptor.getValue().getUuid());
        assertEquals(updateRegisterPayloadSample.firstName(), userArgumentCaptor.getValue().getPersonalInfo().getFirstName());
    }

    @Test
    public void update_ShouldValidateEmailUniqueness() {
        when(userRepository.findByEmail(updateRegisterPayloadSample.email())).thenReturn(Optional.of(userSample));

        assertThatThrownBy(() -> userService.update(UUID.randomUUID(), updateRegisterPayloadSample))
                .isInstanceOf(InvalidArgumentException.class)
                .hasMessage("Given already occupied email");
        verify(userRepository).findByEmail(updateRegisterPayloadSample.email());
        verify(userRepository, never()).save(any());
    }

    @Test
    public void update_ShouldThrowNotFoundException() {
        UUID inputUserUuid = UUID.randomUUID();
        when(userRepository.findByEmail(updateRegisterPayloadSample.email())).thenReturn(Optional.empty());
        when(userRepository.findByUuid(inputUserUuid)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(inputUserUuid, updateRegisterPayloadSample))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No such users found");

        verify(userRepository).findByEmail(updateRegisterPayloadSample.email());
        verify(userRepository).findByUuid(inputUserUuid);
        verify(userRepository, never()).save(any());
    }

    @Test
    public void delete_ShouldFireEmployee() {
        userSample.setRole(Role.ADMIN);
        when(userRepository.findByUuid(userSample.getUuid())).thenReturn(Optional.of(userSample));

        userService.delete(userSample.getUuid());

        verify(userRepository).findByUuid(userSample.getUuid());
        verify(userRepository).save(userArgumentCaptor.capture());
        assertEquals(userSample.getUuid(), userArgumentCaptor.getValue().getUuid());
        assertEquals(Role.CLIENT, userArgumentCaptor.getValue().getRole());
        verify(userRepository, never()).delete(userSample);
        verifyNoInteractions(authService);
    }

    @Test
    public void delete_ShouldDeleteClient_And_LogoutHim() {
        when(userRepository.findByUuid(userSample.getUuid())).thenReturn(Optional.of(userSample));

        userService.delete(userSample.getUuid());

        verify(userRepository).findByUuid(userSample.getUuid());
        verify(userRepository).delete(userSample);
        verify(authService).logout(userSample.getUuid());
        verify(userRepository, never()).save(userSample);
    }

    @Test
    public void delete_ShouldThrowNotFoundException() {
        when(userRepository.findByUuid(userSample.getUuid())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(userSample.getUuid()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No such users found");

        verify(userRepository).findByUuid(userSample.getUuid());
        verify(userRepository, never()).save(userSample);
        verifyNoInteractions(authService);
    }

    @Test
    public void getById_ShouldReturnUserDTO() {
        UserDTO expectedUserDTO = userMapper.mapUserToUserDTO(userSample);
        when(userRepository.findByUuid(userSample.getUuid())).thenReturn(Optional.of(userSample));

        UserDTO actualUserDTO = userService.getById(userSample.getUuid());

        verify(userRepository).findByUuid(userSample.getUuid());
        //first when creating expected dto
        verify(userMapper, times(2)).mapUserToUserDTO(userSample);
        assertEquals(expectedUserDTO, actualUserDTO);
    }

    @Test
    public void getById_ThrowsNotFoundException() {
        when(userRepository.findByUuid(userSample.getUuid())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.getById(userSample.getUuid()))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("No such users found");
        verify(userRepository).findByUuid(userSample.getUuid());
    }

    @Test
    public void listAll_ReturnsListUserDTO() {
        List<UserDTO> expectedUserDTOList = List.of(
                userMapper.mapUserToUserDTO(userSample),
                userMapper.mapUserToUserDTO(userSample)
        );
        when(userRepository.findAll()).thenReturn(List.of(userSample, userSample));

        List<UserDTO> actualUserDTOList = userService.listAll();

        verify(userRepository).findAll();
        //first when creating expected dtos
        verify(userMapper, times(4)).mapUserToUserDTO(userSample);
        assertIterableEquals(expectedUserDTOList, actualUserDTOList);
    }
}