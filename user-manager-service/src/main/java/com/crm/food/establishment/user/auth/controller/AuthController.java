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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.REFRESH_TOKEN_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.UUID_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationRegexps.REFRESH_TOKEN_REGEXP;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    public static final String AUTH_LOGIN_PATH = "/api/auth/login";
    public static final String AUTH_REFRESH_PATH = "/api/auth/refresh";
    public static final String AUTH_LOGOUT_PATH = "/api/auth/logout/{userId}";

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenPair> login(@RequestBody @Valid CredentialsDTO credentials) {
        TokenPair tokenPair = authService.login(credentials);

        return ResponseEntity.ok().body(tokenPair);
    }

    @PostMapping("/refresh")
    public ResponseEntity<String> refresh(
            @RequestParam @Pattern(regexp = REFRESH_TOKEN_REGEXP, message = REFRESH_TOKEN_MESSAGE)
            String refreshToken
    ) {
        String accessToken = authService.refresh(refreshToken);

        return ResponseEntity.ok().body(accessToken);
    }

    @PostMapping("/logout/{userId}")
    public ResponseEntity<Void> logout(@PathVariable @UUID(message = UUID_MESSAGE) String userId) {
        authService.logout(java.util.UUID.fromString(userId));

        return ResponseEntity.ok().build();
    }
}