package com.crmfoodestablishment.user_auth_service.auth_sevice.service.impl;

import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.TokenPairResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserCreationRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.FailedRegistrationException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.InvalidTokenException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.InvalidUserCredentialsException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.AuthService;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.JwtService;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.token.RefreshToken;

import com.crmfoodestablishment.user_auth_service.user_manager.exception.NotFoundException;
import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;
import com.crmfoodestablishment.user_auth_service.user_manager.services.UserService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenPairResponsePayload login(LoginRequestPayload credentials) {
        //multiple refresh i.e. sessions allowed for same user because
        //he may also log in from mobile

        User user;
        try {
            user = userService.findByEmail(credentials.getEmail());
        } catch (NotFoundException e) {
            throw new InvalidUserCredentialsException("Wrong email");
        }

        if(!user.getPassword().equals(credentials.getPassword())) {
            throw new InvalidUserCredentialsException("Wrong password");
        }

        String accessToken = jwtService.issueAccessToken(user);
        String refreshToken = jwtService.issueRefreshToken(user);

        log.info("User: " + user.getEmail() + " logged");
        return new TokenPairResponsePayload(accessToken, refreshToken);
    }

    @Override
    public RegisterResponsePayload register(UserCreationRequestPayload creationData) {
        if (userService.existsByEmail(creationData.getEmail())) {
            throw new FailedRegistrationException("Registration exception: given already occupied email");
        }

        creationData.setPassword(
                passwordEncoder.encode(creationData.getPassword())
        );
        User createdUser = userService.createUser(creationData);

        String accessToken = jwtService.issueAccessToken(createdUser);
        String refreshToken = jwtService.issueRefreshToken(createdUser);

        String createdUserUrl = UserController.USER_CRUD_PATH + "/" + createdUser.getId();

        log.info("User: " + createdUser.getEmail() + " registered");
        return new RegisterResponsePayload(
                createdUserUrl,
                new TokenPairResponsePayload(accessToken, refreshToken)
        );
    }

    @Override
    public String refresh(String refreshToken) {
        RefreshToken parsedRefreshToken = jwtService.parseRefreshToken(refreshToken);

        User user;
        try {
            user = userService.findByUuid(parsedRefreshToken.claims().sub());
        } catch (NotFoundException e) {
            throw new InvalidTokenException("Invalid subject: no such user with that id");
        }

        log.info("User: " + user.getUuid() + " refreshed access token");
        return jwtService.issueAccessToken(user);
    }

    @Override
    public void logout(UUID userUuid) {
        jwtService.invalidateRefreshToken(userUuid);
        log.info("User: " + userUuid + " logged out");
    }
}
