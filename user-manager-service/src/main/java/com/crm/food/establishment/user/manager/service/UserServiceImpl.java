package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;
import com.crm.food.establishment.user.manager.repository.UserRepository;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.dto.RegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UpdateUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserMapper userMapper;

    @Override
    public RegisterUserResponseDTO register(
            RegisterUserRequestDTO creationData
    ) {
        User savedUser = createUser(creationData);
        TokenPair tokenPair = jwtService.issueTokenPair(savedUser);

        log.info("User: " + savedUser.getEmail() + " registered");

        return new RegisterUserResponseDTO(
                savedUser.getUuid(),
                tokenPair
        );
    }

    @Override
    public void update(
            UUID userUuid,
            UpdateUserRequestDTO updatedData
    ) {
        AtomicReference<User> userToUpdate = new AtomicReference<>();

        userRepository.findByEmail(updatedData.getEmail()).ifPresent(
                foundUser -> {
                    if (foundUser.getUuid().equals(userUuid)) {
                        userToUpdate.set(foundUser);
                    } else {
                        logAndThrowException(
                                new InvalidArgumentException("Given already occupied email")
                        );
                    }
                }
        );

        if (userToUpdate.get() == null) {
            userRepository.findByUuid(userUuid).ifPresentOrElse(
                    userToUpdate::set,
                    this::logAndThrowNotFoundException
            );
        }

        userMapper.mapUpdateUserRequestDTOToUser(
                updatedData,
                userToUpdate.get()
        );

        userRepository.save(userToUpdate.get());
        log.info("Updated user: " + userUuid);
    }

    @Override
    public void delete(
            UUID userUuid
    ) {
        AtomicReference<User> userToDelete = new AtomicReference<>();
        userRepository.findByUuid(userUuid).ifPresentOrElse(
                userToDelete::set,
                this::logAndThrowNotFoundException
        );

        Role userToDeleteRole = userToDelete.get().getRole();
        if (userToDeleteRole == Role.ADMIN || userToDeleteRole == Role.EMPLOYEE) {
            userToDelete.get().setRole(Role.CLIENT);

            userRepository.save(userToDelete.get());

            log.info("Fired " + userToDeleteRole.name() + ": " + userUuid);
        } else if (userToDeleteRole == Role.CLIENT) {
            userRepository.delete(userToDelete.get());

            log.info("Deleted user: " + userUuid);
        }
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

    private User createUser(
            RegisterUserRequestDTO creationData
    ) {
        Optional<User> user = userRepository.findByEmail(creationData.getEmail());
        User userToRegister = null;

        if (
                user.isPresent()
                && user.get().getRole() == Role.CLIENT
                && (creationData.getRole() == Role.ADMIN || creationData.getRole() == Role.EMPLOYEE)
        ) {
            userToRegister = user.get();
        } else if (user.isEmpty()) {
            userToRegister = new User(UUID.randomUUID());
        } else {
            logAndThrowException(
                    new InvalidArgumentException("Given already occupied email")
            );
        }

        userMapper.mapRegisterUserRequestDTOToUser(
                creationData,
                userToRegister
        );

        return userRepository.save(userToRegister);
    }

    private void logAndThrowException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        throw exception;
    }

    private void logAndThrowNotFoundException() {
        var error = new NotFoundException("No such users found");
        log.error(error.getMessage(), error);
        throw error;
    }
}
