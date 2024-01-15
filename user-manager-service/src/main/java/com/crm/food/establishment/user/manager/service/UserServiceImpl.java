package com.crm.food.establishment.user.manager.service;

import com.crm.food.establishment.user.auth.service.AuthService;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;
import com.crm.food.establishment.user.manager.repository.UserRepository;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDto;
import com.crm.food.establishment.user.manager.dto.UserDto;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.mapper.UserMapper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final AuthService authService;
    private final UserMapper userMapper;

    @Override
    public RegisterUserResponseDto registerUser(UpdateRegisterUserRequestDto creationDto) {
        User savedUser = createOrUpdateExistingUser(creationDto);
        TokenPair issuedTokenPair = jwtService.issueTokenPair(savedUser);

        return new RegisterUserResponseDto(savedUser.getUuid(), issuedTokenPair);
    }

    @Override
    public void updateUser(UUID userUuid, UpdateRegisterUserRequestDto updateDto) {
        Optional<User> userToUpdate = userRepository.findByEmail(updateDto.email());

        if (userToUpdate.isPresent() && !userToUpdate.get().getUuid().equals(userUuid)) {
            throw new InvalidArgumentException("Given already occupied email");
        } else if (userToUpdate.isEmpty()) {
            userToUpdate = Optional.of(userRepository.findByUuid(userUuid)
                    .orElseThrow(() -> new NotFoundException("No such users found"))
            );
        }

        userRepository.save(userMapper.mapUpdateRegisterUserRequestDtoToUser(
                userToUpdate.get().getId(), userUuid, updateDto
        ));
        log.info("Updated user with uuid: [" + userUuid + "]");
    }

    @Override
    public void deleteUser(UUID userUuid) {
        User userToDelete = userRepository.findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("No such users found"));

        Role userToDeleteRole = userToDelete.getRole();
        if (userToDeleteRole == Role.ADMIN || userToDeleteRole == Role.EMPLOYEE) {
            userToDelete.setRole(Role.CLIENT);
            userRepository.save(userToDelete);

            log.info("Role of user [" + userUuid + "] was changed from ["  + userToDeleteRole.name() + "] to [" + Role.CLIENT.name() + "]");
        } else if (userToDeleteRole == Role.CLIENT) {
            userRepository.delete(userToDelete);
            authService.logout(userToDelete.getUuid());

            log.info("Deleted user with uuid: [" + userUuid + "]");
        }
    }

    @Override
    public UserDto getUserById(UUID userUuid) {
        User foundUser = userRepository
                .findByUuid(userUuid)
                .orElseThrow(() -> new NotFoundException("No such users found"));

        return userMapper.mapUserToUserDto(foundUser);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(userMapper::mapUserToUserDto)
                .toList();
    }

    private User createOrUpdateExistingUser(UpdateRegisterUserRequestDto creationData) {
        Optional<User> fetchedUser = userRepository.findByEmail(creationData.email());

        if (
                fetchedUser.isPresent()
                && fetchedUser.get().getRole() == Role.CLIENT
                && (creationData.role() == Role.ADMIN || creationData.role() == Role.EMPLOYEE)
        ) {
            User userToUpdate = userMapper.mapUpdateRegisterUserRequestDtoToUser(
                    fetchedUser.get().getId(), fetchedUser.get().getUuid(), creationData
            );
            userToUpdate.setPassword(fetchedUser.get().getPassword());

            User updatedUser = userRepository.save(userToUpdate);
            log.info("Updated user with uuid: [" + updatedUser.getUuid() + "]");
            return updatedUser;
        } else if (fetchedUser.isEmpty()) {
            User userToRegister = userMapper.mapUpdateRegisterUserRequestDtoToUser(
                    null, UUID.randomUUID(), creationData
            );

            User registeredUser = userRepository.save(userToRegister);
            log.info("User with email: [" + registeredUser.getEmail() + "] registered");
            return registeredUser;
        } else {
            throw new InvalidArgumentException("Given already occupied email");
        }
    }
}
