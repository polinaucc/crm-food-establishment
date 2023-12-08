package com.crm.food.establishment.user.auth.controller;

import com.crm.food.establishment.user.auth.service.AuthService;
import com.crm.food.establishment.user.auth.dto.CredentialsDTO;
import com.crm.food.establishment.user.auth.token.TokenPair;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

import org.hibernate.validator.constraints.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    public static final String AUTH_PATH = "/api/auth";

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(
            @RequestBody
            @Valid
            CredentialsDTO credentials
    ) {
        TokenPair tokenPair = authService.login(credentials);

        return ResponseEntity
                .ok()
                .body(tokenPair);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @RequestParam(name = "refreshToken")
            @Pattern(regexp = "^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)")
            String refreshToken
    ) {
        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity
                .ok()
                .body(accessToken);
    }

    @PostMapping("/logout/{userUuid}")
    public ResponseEntity<Void> logout(
            @PathVariable
            @UUID
            String userUuid
    ) {
        authService.logout(
                java.util.UUID.fromString(userUuid)
        );

        return ResponseEntity.ok().build();
    }
}
