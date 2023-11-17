package com.crmfoodestablishment.userauthservice.usermanager.controller;

import com.crmfoodestablishment.userauthservice.usermanager.dto.RegisterUserRequestDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.RegisterUserResponseDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UpdateUserRequestDTO;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
@Validated
public class UserController {

    public static final String USER_PATH = "/api/user";
    public static final String USER_ID_PATH_VAR = "/{userId}";
    public static final String USER_PATH_ID = USER_PATH + USER_ID_PATH_VAR;

    private final UserService userService;

    @PostMapping
    public ResponseEntity<RegisterUserResponseDTO> registerUser(
            @RequestBody
            @Valid
            RegisterUserRequestDTO creationData
    ) {
        RegisterUserResponseDTO registerResponse = userService.register(
                creationData
        );

        return new ResponseEntity<>(registerResponse, HttpStatus.CREATED);
    }

    @PutMapping(USER_ID_PATH_VAR)
    public ResponseEntity<Void> updateUser(
            @PathVariable
            @UUID(allowNil = false)
            String userId,
            @RequestBody
            @Valid
            UpdateUserRequestDTO updatedData
    ) {
        userService.update(
                java.util.UUID.fromString(userId),
                updatedData
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(USER_ID_PATH_VAR)
    public ResponseEntity<Void> deleteUser(
            @PathVariable
            @UUID(allowNil = false)
            String userId
    ) {
        userService.delete(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.noContent().build();
    }

    @GetMapping(USER_ID_PATH_VAR)
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable
            @UUID(allowNil = false)
            String userId
    ) {
        UserDTO userResponsePayload = userService.getById(
                java.util.UUID.fromString(userId)
        );

        return ResponseEntity.ok(userResponsePayload);
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> foundUsers = userService.listAll();

        return ResponseEntity.ok(foundUsers);
    }
}