package com.crmfoodestablishment.usermanager.controllers;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.usermanager.exceptions.InvalidTokenException;
import com.crmfoodestablishment.usermanager.exceptions.NotFoundException;
import com.crmfoodestablishment.usermanager.exceptions.WrongUserCredentialsException;
import com.crmfoodestablishment.usermanager.controllers.payloads.LoginRequestPayload;
import com.crmfoodestablishment.usermanager.controllers.payloads.TokenPairResponsePayload;
import com.crmfoodestablishment.usermanager.controllers.payloads.UserCreationRequestPayload;
import com.crmfoodestablishment.usermanager.security.JwtService;
import com.crmfoodestablishment.usermanager.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    public static final String AUTH_PATH = "/api/v1/auth";

    private final JwtService jwtService;
    private final UserService userService;

    @PostMapping(AUTH_PATH + "/login")
    public ResponseEntity<TokenPairResponsePayload> login(
            @RequestBody @Valid LoginRequestPayload credentials
    ) {
        User user;
        try {
            user = userService.findByEmail(credentials.getEmail());
        } catch (NotFoundException e) {
            throw new WrongUserCredentialsException("Wrong email");
        }

        if(!user.getPassword().equals(credentials.getPassword())) {
            throw new WrongUserCredentialsException("Wrong password");
        }

        String accessToken = jwtService.issueAccessToken(user);
        String refreshToken = jwtService.issueRefreshToken(user);

        return ResponseEntity
                .ok()
                .body(new TokenPairResponsePayload(accessToken, refreshToken));
    }

    @PostMapping(AUTH_PATH + "/register")
    public ResponseEntity<TokenPairResponsePayload> register(
            @RequestBody @Valid UserCreationRequestPayload creationData
    ) {
        User createdUser = userService.createUser(creationData);

        String accessToken = jwtService.issueAccessToken(createdUser);
        String refreshToken = jwtService.issueRefreshToken(createdUser);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", USER_CRUD_PATH + "/" + createdUser.getId());

        return ResponseEntity
                .ok()
                .headers(headers)
                .body(new TokenPairResponsePayload(accessToken, refreshToken));
    }

    @PostMapping(AUTH_PATH + "/refresh")
    //Як транспортувати решреш токен?
    public ResponseEntity<String> refresh(
            @RequestBody String refreshToken
    ) {
        if(!jwtService.validateRefreshToken(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token given");
        }

        User user;
        try {
            user = userService.findByEmail(
                    jwtService.parseRefreshToken()
                            .claims()
                            .getSubject()
            );
        } catch (NotFoundException e) {
            throw new WrongUserCredentialsException("Wrong email");
        }

        String accessToken = jwtService.issueAccessToken(user);

        return ResponseEntity
                .ok()
                .body(accessToken);
    }

    @PostMapping(AUTH_PATH + "/logout")
    public ResponseEntity<Void> logout(
            @RequestBody String userEmail
    ) {
        jwtService.invalidateRefreshToken(userEmail);

        return ResponseEntity.ok().build();
    }
}
