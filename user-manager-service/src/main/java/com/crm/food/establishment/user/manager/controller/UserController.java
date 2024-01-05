package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.manager.dto.RegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UpdateUserRequestDTO;
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

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    public static final String USER_PATH = "/api/user";
    public static final String USER_ID_PATH_VAR = "/{userId}";

    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> registerUser(
            @RequestBody @Valid RegisterUserRequestDTO creationDTO
    ) {
        RegisterUserResponseDTO registerResponse = userService.register(creationDTO);

        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PutMapping(USER_ID_PATH_VAR)
    public ResponseEntity<Void> updateUser(
            @PathVariable @UUID String userId,
            @RequestBody @Valid UpdateUserRequestDTO updateDTO
    ) {
        userService.update(java.util.UUID.fromString(userId), updateDTO);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(USER_ID_PATH_VAR)
    public ResponseEntity<Void> deleteUser(@PathVariable @UUID String userId) {
        userService.delete(java.util.UUID.fromString(userId));

        return ResponseEntity.noContent().build();
    }

    @GetMapping(USER_ID_PATH_VAR)
    public ResponseEntity<UserDTO> getUserById(@PathVariable @UUID String userId) {
        UserDTO fetchedUser = userService.getById(java.util.UUID.fromString(userId));

        return ResponseEntity.ok(fetchedUser);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> fetchedUserList = userService.listAll();

        return ResponseEntity.ok(fetchedUserList);
    }
}