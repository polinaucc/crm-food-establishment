package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDto;
import com.crm.food.establishment.user.manager.dto.UserDto;
import com.crm.food.establishment.user.manager.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    public static final String USER_ID_PATH_VAR = "/{userId}";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponseDto> registerUser(
            @RequestBody @Valid UpdateRegisterUserRequestDto creationDTO
    ) {
        RegisterUserResponseDto registerResponse = userService.registerUser(creationDTO);

        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PutMapping(USER_ID_PATH_VAR)
    public ResponseEntity<Void> updateUser(
            @PathVariable @UUID(message = INVALID_UUID_MESSAGE) String userId,
            @RequestBody @Valid UpdateRegisterUserRequestDto updateDTO
    ) {
        userService.updateUser(java.util.UUID.fromString(userId), updateDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(USER_ID_PATH_VAR)
    public ResponseEntity<Void> deleteUser(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String userId) {
        userService.deleteUser(java.util.UUID.fromString(userId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping(USER_ID_PATH_VAR)
    public ResponseEntity<UserDto> getUserById(@PathVariable @UUID(message = INVALID_UUID_MESSAGE) String userId) {
        UserDto fetchedUser = userService.getUserById(java.util.UUID.fromString(userId));

        return ResponseEntity.ok(fetchedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> listUsers() {
        List<UserDto> fetchedUserList = userService.getAllUsers();

        return ResponseEntity.ok(fetchedUserList);
    }
}