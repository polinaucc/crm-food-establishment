package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.auth.service.AuthService;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;
import com.crm.food.establishment.user.manager.repository.UserRepository;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
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
    private final AuthService authService;
    private final UserMapper userMapper;

    @Override
    public RegisterUserResponseDTO register(UpdateRegisterUserRequestDTO creationDTO) {
        User savedUser = createUser(creationDTO);
        TokenPair issuedTokenPair = jwtService.issueTokenPair(savedUser);

        log.info("User: " + savedUser.getEmail() + " registered");
        return new RegisterUserResponseDTO(savedUser.getUuid(), issuedTokenPair);
    }

    @Override
    public void update(UUID userUuid, UpdateRegisterUserRequestDTO updateDTO) {
        AtomicReference<User> userToUpdate = new AtomicReference<>();

        userRepository.findByEmail(updateDTO.email()).ifPresent(
                foundUser -> {
                    if (foundUser.getUuid().equals(userUuid)) {
                        userToUpdate.set(foundUser);
                    } else {
                        throw new InvalidArgumentException("Given already occupied email");
                    }
                }
        );

        if (userToUpdate.get() == null) {
            userToUpdate.set(userRepository.findByUuid(userUuid)
                    .orElseThrow(() -> new NotFoundException("No such users found"))
            );
        }

        userMapper.mapUpdateRegisterUserRequestDTOToUser(updateDTO, userToUpdate.get());

        userRepository.save(userToUpdate.get());
        log.info("Updated user: " + userUuid);
    }

    @Override
    public void delete(UUID userUuid) {
        User userToDelete = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("No such users found"));

        Role userToDeleteRole = userToDelete.getRole();
        if (userToDeleteRole == Role.ADMIN || userToDeleteRole == Role.EMPLOYEE) {
            userToDelete.setRole(Role.CLIENT);
            userRepository.save(userToDelete);

            log.info("Fired " + userToDeleteRole.name() + ": " + userUuid);
        } else if (userToDeleteRole == Role.CLIENT) {
            userRepository.delete(userToDelete);
            authService.logout(userToDelete.getUuid());

            log.info("Deleted user: " + userUuid);
        }
    }

    @Override
    public UserDTO getById(UUID userUuid) {
        User foundUser = userRepository
                .findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("No such users found"));

        return userMapper.mapUserToUserDTO(foundUser);
    }

    @Override
    public List<UserDTO> listAll() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::mapUserToUserDTO)
                .toList();
    }

    private User createUser(UpdateRegisterUserRequestDTO creationData) {
        Optional<User> user = userRepository.findByEmail(creationData.email());
        User userToRegister;

        if (
                user.isPresent()
                && user.get().getRole() == Role.CLIENT
                && (creationData.role() == Role.ADMIN || creationData.role() == Role.EMPLOYEE)
        ) {
            userToRegister = user.get();
        } else if (user.isEmpty()) {
            userToRegister = new User(UUID.randomUUID());
        } else {
            throw new InvalidArgumentException("Given already occupied email");
        }

        userMapper.mapUpdateRegisterUserRequestDTOToUser(creationData, userToRegister);

        return userRepository.save(userToRegister);
    }
}
