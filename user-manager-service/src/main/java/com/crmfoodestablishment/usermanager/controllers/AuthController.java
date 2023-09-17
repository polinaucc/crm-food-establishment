package com.crmfoodestablishment.usermanager.controllers;

import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.usermanager.exceptions.InvalidTokenException;
import com.crmfoodestablishment.usermanager.exceptions.WrongUserCredentialsException;
import com.crmfoodestablishment.usermanager.payloads.LoginRequestPayload;
import com.crmfoodestablishment.usermanager.payloads.RefreshPayload;
import com.crmfoodestablishment.usermanager.payloads.TokenPairResponsePayload;
import com.crmfoodestablishment.usermanager.payloads.UserCreationRequestPayload;

import com.crmfoodestablishment.usermanager.services.JwtService;
import com.crmfoodestablishment.usermanager.services.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
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

        String accessToken = jwtService.issueAccessToken(createdUser.getUserPermissionList());
        String refreshToken = jwtService.issueRefreshToken();

        return ResponseEntity.ok(new TokenPairResponsePayload(accessToken, refreshToken));
    }

    @PostMapping(AUTH_PATH + "/register")
    public ResponseEntity<TokenPairResponsePayload> register(
            @RequestBody @Valid UserCreationRequestPayload creationData
    ) {
        User createdUser = userService.createUser(creationData);

        //Питання - може краще передавати в issueTokenPair цілий об'єкт
        String accessToken = jwtService.issueAccessToken(createdUser.getUserPermissionList());
        String refreshToken = jwtService.issueRefreshToken();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Location", USER_PATH + "/" + createdUser.getId());

        return new ResponseEntity<>(new TokenPairResponsePayload(accessToken, refreshToken), headers, HttpStatus.OK);
    }

    @PostMapping(AUTH_PATH + "/refresh")
    //Як транспортувати решреш токен?
    //І що краще закодувати айді(імейл) в рефреш токен, або окремо?
    public ResponseEntity<String> refresh(@RequestBody @Valid RefreshPayload refreshPayload) {
        if(!jwtService.validateRefreshToken(refreshPayload.getRefreshToken())) {
            throw new InvalidTokenException("Invalid refresh token given");
        }

        User user;
        try {
            user = userService.findByEmail(refreshPayload.getEmail());
        } catch (NotFoundException e) {
            throw new WrongUserCredentialsException("Wrong email");
        }

        return ResponseEntity.ok(new TokenPairResponsePayload(accessToken, refreshToken));
    }

    @PostMapping(AUTH_PATH + "/logout")
    public ResponseEntity<Void> logout(@RequestBody @Valid String userEmail) {
        jwtService.invalidateRefreshToken(userEmail);

        return ResponseEntity.ok().build();
    }
}
