package com.crmfoodestablishment.userauthservice.usermanager.controller;

import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.UpdateUserRequestPayload;
import com.crmfoodestablishment.userauthservice.usermanager.controller.payload.UserRegistrationRequestPayload;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import com.crmfoodestablishment.userauthservice.usermanager.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Set;

@RestController
@RequiredArgsConstructor
public class UserController {

    public static final String USER_PATH = "/api/v1/user";
    public static final String USER_PATH_ID = USER_PATH + "/{userId}";

    private final UserService userService;

    @PostMapping(USER_PATH + "/admin")
    public ResponseEntity<TokenPair> registerAdmin(
            @RequestBody
            @Valid
            UserRegistrationRequestPayload creationData
    ) {
        RegisterResponsePayload registerResponse = userService.register(creationData, Role.ADMIN);

        return ResponseEntity
                .created(URI.create(registerResponse.getUrlOfCreatedUser()))
                .body(registerResponse.getTokenPair());
    }

    @PostMapping(USER_PATH + "/employee")
    public ResponseEntity<TokenPair> registerEmployee(
            @RequestBody
            @Valid
            UserRegistrationRequestPayload creationData
    ) {
        RegisterResponsePayload registerResponse = userService.register(creationData, Role.EMPLOYEE);

        return ResponseEntity
                .created(URI.create(registerResponse.getUrlOfCreatedUser()))
                .body(registerResponse.getTokenPair());
    }

    @PostMapping(USER_PATH + "/customer")
    public ResponseEntity<TokenPair> registerCustomer(
            @RequestBody
            @Valid
            UserRegistrationRequestPayload creationData
    ) {
        RegisterResponsePayload registerResponse = userService.register(creationData, Role.CUSTOMER);

        return ResponseEntity
                .created(URI.create(registerResponse.getUrlOfCreatedUser()))
                .body(registerResponse.getTokenPair());
    }

    @PutMapping(USER_PATH_ID)
    @PreAuthorize(
            //I would like to use here Permission.ADMIN.name(),
            //but do it such full name of Permission need to be specified
            "hasRole('ROLE_ADMIN') or #userId == authentication.principal"
    )
    public ResponseEntity<Void> updateUser(
            @PathVariable
            @UUID(allowNil = false)
            String userId,
            @RequestBody
            @Valid
            UpdateUserRequestPayload updatedData
    ) {
        userService.update(
                java.util.UUID.fromString(userId),
                updatedData
        );

        return ResponseEntity.noContent().build();
    }

    @PutMapping(USER_PATH_ID + "/role")
    public ResponseEntity<Void> updateUserRole(
            @PathVariable
            @UUID(allowNil = false)
            String userId,
            @RequestBody
            Set<Role> updatedRoles
    ) {
        userService.updateRoles(
                java.util.UUID.fromString(userId),
                updatedRoles
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping(USER_PATH_ID)
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') or #userId == authentication.principal"
    )
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

    @GetMapping(USER_PATH_ID)
    @PreAuthorize(
            "hasRole('ROLE_ADMIN') or #userId == authentication.principal"
    )
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

    @GetMapping(USER_PATH)
    public ResponseEntity<List<UserDTO>> listUsers() {
        List<UserDTO> foundUsers = userService.listAll();

        return ResponseEntity.ok(foundUsers);
    }
}