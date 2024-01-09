package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
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

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.UUID_MESSAGE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    public static final String USER_PATH = "/api/user";
    public static final String USER_PATH_WITH_ID = "/api/user/{userId}";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> registerUser(
            @RequestBody @Valid UpdateRegisterUserRequestDTO creationDTO
    ) {
        RegisterUserResponseDTO registerResponse = userService.register(creationDTO);

        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<Void> updateUser(
            @PathVariable @UUID(message = UUID_MESSAGE) String userId,
            @RequestBody @Valid UpdateRegisterUserRequestDTO updateDTO
    ) {
        userService.update(java.util.UUID.fromString(userId), updateDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable @UUID(message = UUID_MESSAGE) String userId) {
        userService.delete(java.util.UUID.fromString(userId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable @UUID(message = UUID_MESSAGE) String userId) {
        UserDTO fetchedUser = userService.getById(java.util.UUID.fromString(userId));

        return ResponseEntity.ok(fetchedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> fetchedUserList = userService.listAll();

        return ResponseEntity.ok(fetchedUserList);
    }
}