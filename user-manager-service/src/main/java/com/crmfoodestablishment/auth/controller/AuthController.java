package com.crmfoodestablishment.auth.controller;

import com.crmfoodestablishment.auth.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.auth.controller.payload.UserCreationRequestPayload;
import com.crmfoodestablishment.auth.service.AuthService;
import com.crmfoodestablishment.auth.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.auth.controller.payload.TokenPairResponsePayload;

import jakarta.validation.Valid;
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
            @RequestBody String refreshToken
    ) {
        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity
                .ok()
                .body(accessToken);
    }

    @PostMapping(AUTH_PATH + "/logout")
    public ResponseEntity<Void> logout(
            @RequestBody String userEmail
    ) {
        authService.logout(userEmail);

        return ResponseEntity.ok().build();
    }
}
