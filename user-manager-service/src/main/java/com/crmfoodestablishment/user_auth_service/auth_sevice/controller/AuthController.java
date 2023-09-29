package com.crmfoodestablishment.user_auth_service.auth_sevice.controller;

import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserCreationRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.AuthService;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.TokenPairResponsePayload;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    public static final String AUTH_PATH = "/api/v1/auth";

    private final AuthService authService;

    @PostMapping(AUTH_PATH + "/login")
    public ResponseEntity<TokenPairResponsePayload> login(
            @RequestBody @Valid LoginRequestPayload credentials
    ) {
        TokenPairResponsePayload tokenPair = authService.login(credentials);

        return ResponseEntity
                .ok()
                .body(tokenPair);
    }

    @PostMapping(AUTH_PATH + "/register")
    public ResponseEntity<RegisterResponsePayload> register(
            @RequestBody @Valid UserCreationRequestPayload creationData
    ) {
        RegisterResponsePayload registerResponse = authService.register(creationData);

        return ResponseEntity
                .ok()
                .body(registerResponse);
    }

    @PostMapping(AUTH_PATH + "/refresh")
    public ResponseEntity<String> refresh(
            @RequestBody
            @Pattern(regexp = "/^([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_=]+)\\.([a-zA-Z0-9_\\-\\+\\/=]*)/")
            String refreshToken
    ) {
        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity
                .ok()
                .body(accessToken);
    }

    @PostMapping(AUTH_PATH + "/logout")
    public ResponseEntity<Void> logout(
            @RequestBody @Email String userEmail
    ) {
        authService.logout(userEmail);

        return ResponseEntity.ok().build();
    }
}
