package com.crmfoodestablishment.userauthservice.authservice.service.impl;

import com.crmfoodestablishment.userauthservice.authservice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import com.crmfoodestablishment.userauthservice.usermanager.dto.UserDTO;
import com.crmfoodestablishment.userauthservice.authservice.exception.InvalidTokenException;
import com.crmfoodestablishment.userauthservice.authservice.exception.InvalidUserCredentialsException;
import com.crmfoodestablishment.userauthservice.authservice.service.AuthService;
import com.crmfoodestablishment.userauthservice.authservice.service.JwtService;
import com.crmfoodestablishment.userauthservice.authservice.token.RefreshToken;

import com.crmfoodestablishment.userauthservice.usermanager.exception.NotFoundException;
import com.crmfoodestablishment.userauthservice.usermanager.service.UserService;

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
    public TokenPair login(LoginRequestPayload credentials) {
        UserDTO user;
        try {
            user = userService.getByEmail(credentials.getEmail());
        } catch (NotFoundException e) {
            throw new InvalidUserCredentialsException("Wrong email");
        }

        if(!user.getPassword().equals(
                passwordEncoder.encode(credentials.getPassword())
        )) {
            throw new InvalidUserCredentialsException("Wrong password");
        }

        TokenPair tokenPair = jwtService.issueTokenPair(user);

        log.info("User: " + user.getEmail() + " logged");
        return tokenPair;
    }

    @Override
    public String refresh(String refreshToken) {
        //TODO implement maximum number of access tokens for one user

        RefreshToken parsedRefreshToken = jwtService.parseRefreshToken(refreshToken);

        UserDTO user;
        try {
            user = userService.getById(parsedRefreshToken.claims().sub());
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
