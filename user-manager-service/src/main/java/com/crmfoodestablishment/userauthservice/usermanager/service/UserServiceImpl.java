package com.crmfoodestablishment.userauthservice.usermanager.service;

import com.crmfoodestablishment.userauthservice.usermanager.exception.FailedRegistrationException;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import com.crmfoodestablishment.userauthservice.authservice.service.JwtService;
import com.crmfoodestablishment.userauthservice.usermanager.controller.UserController;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.UpdateUserRequestPayload;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.UserRegistrationRequestPayload;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import com.crmfoodestablishment.userauthservice.usermanager.entity.User;
import com.crmfoodestablishment.userauthservice.usermanager.entity.UserPersonalInfo;
import com.crmfoodestablishment.userauthservice.usermanager.exception.InvalidArgumentException;
import com.crmfoodestablishment.userauthservice.usermanager.exception.NotFoundException;
import com.crmfoodestablishment.userauthservice.usermanager.mapper.UserMapper;
import com.crmfoodestablishment.userauthservice.usermanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    public UserDTO getByEmail(String email) {
        AtomicReference<UserDTO> userDTO = new AtomicReference<>();

        userRepository.findByEmail(email).ifPresentOrElse(
                foundUser -> userDTO.set(
                        userMapper.mapUserToUserDTO(foundUser)
                ),
                this::logAndThrowNotFoundException
        );

        return userDTO.get();
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public RegisterResponsePayload register(
            UserRegistrationRequestPayload creationData,
            Role role
    ) {
        UserDTO savedUser = createUser(creationData, role);
        TokenPair tokenPair = jwtService.issueTokenPair(savedUser);

        String createdUserUrl = UserController.USER_PATH + "/" + savedUser.getUuid();

        log.info("User: " + savedUser.getEmail() + " registered");
        return new RegisterResponsePayload(
                createdUserUrl,
                tokenPair
        );
    }

    @Override
    public void update(
            UUID userUuid,
            UpdateUserRequestPayload updatedData
    ) {
        validateUniquenessOfEmail(
                updatedData.getEmail(),
                userUuid,
                new InvalidArgumentException("Invalid argument exception: given already occupied email")
        );

        userRepository.findByUuid(userUuid).ifPresentOrElse(
                foundUser -> {
                    foundUser.setEmail(updatedData.getEmail());
                    foundUser.setPassword(
                            passwordEncoder.encode(updatedData.getPassword())
                    );
                    foundUser.getPersonalInfo().setFirstName(updatedData.getFirstName());
                    foundUser.getPersonalInfo().setLastName(updatedData.getLastName());
                    foundUser.getPersonalInfo().setBirthday(updatedData.getBirthday());
                    foundUser.getPersonalInfo().setIsMale(updatedData.getIsMale());
                    foundUser.getPersonalInfo().setAddress(updatedData.getEmail());

                    userRepository.save(foundUser);
                    log.info("Updated user: " + userUuid);
                },
                this::logAndThrowNotFoundException
        );
    }

    @Override
    public void updateRoles(
            UUID userUuid,
            Set<Role> updatedRoles
    ) {
        userRepository.findByUuid(userUuid).ifPresentOrElse(
                foundUser -> {
                    foundUser.setRoles(updatedRoles);

                    userRepository.save(foundUser);
                    log.info("Updated roles of user: " + userUuid);
                },
                this::logAndThrowNotFoundException
        );
    }

    @Override
    public void delete(UUID userUuid) {
        if (!userRepository.existsByUuid(userUuid)) {
            logAndThrowNotFoundException();
        }

        userRepository.deleteByUuid(userUuid);
        log.info("Deleted user: " + userUuid);
    }

    @Override
    public UserDTO getById(UUID userUuid) {
        AtomicReference<UserDTO> userDTO = new AtomicReference<>();

        userRepository.findByUuid(userUuid).ifPresentOrElse(
                foundUser -> userDTO.set(
                        userMapper.mapUserToUserDTO(foundUser)
                ),
                this::logAndThrowNotFoundException
        );

        return userDTO.get();
    }

    @Override
    public List<UserDTO> listAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::mapUserToUserDTO)
                .toList();
    }

    private void validateUniquenessOfEmail(
            String email,
            UUID userUuid,
            RuntimeException exception
    ) {
        userRepository.findByEmail(email).ifPresent(
                foundUser -> {
                    if (!foundUser.getUuid().equals(userUuid)) {
                        log.error(exception.getMessage(), exception);
                        throw exception;
                    }
                }
        );
    }

    private UserDTO createUser(
            UserRegistrationRequestPayload creationData,
            Role role
    ) {
        validateUniquenessOfEmail(
                creationData.getEmail(),
                null,
                new FailedRegistrationException("Registration exception: given already occupied email")
        );

        User user = new User(UUID.randomUUID());
        user.setEmail(creationData.getEmail());
        user.setPassword(
                passwordEncoder.encode(creationData.getPassword())
        );
        user.getRoles().add(role);
        user.setPersonalInfo(new UserPersonalInfo());
        user.getPersonalInfo().setFirstName(creationData.getFirstName());
        user.getPersonalInfo().setLastName(creationData.getLastName());
        user.getPersonalInfo().setBirthday(creationData.getBirthday());
        user.getPersonalInfo().setIsMale(creationData.getIsMale());
        user.getPersonalInfo().setAddress(creationData.getEmail());

        return userMapper.mapUserToUserDTO(
                userRepository.save(user)
        );
    }

    private void logAndThrowNotFoundException() {
        var error = new NotFoundException("Given invalid argument: no such users found");
        log.error(error.getMessage(), error);
        throw error;
    }
}
