package com.crmfoodestablishment.user_auth_service.auth_sevice.service;

import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.TokenPairResponsePayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.controller.payload.UserCreationRequestPayload;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.InvalidTokenException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.RegistrationException;
import com.crmfoodestablishment.user_auth_service.user_manager.exception.NotFoundException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.exception.WrongUserCredentialsException;
import com.crmfoodestablishment.user_auth_service.auth_sevice.service.model.RefreshToken;
import com.crmfoodestablishment.user_auth_service.user_manager.entity.User;
import com.crmfoodestablishment.user_auth_service.user_manager.services.UserService;

import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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
            throw new WrongUserCredentialsException("Wrong email");
        }

        if(!user.getPassword().equals(credentials.getPassword())) {
            throw new WrongUserCredentialsException("Wrong password");
        }

        String accessToken = jwtService.issueAccessToken(user);
        String refreshToken = jwtService.issueRefreshToken(user);

        log.info("User: " + user.getEmail() + " logged");
        return new TokenPairResponsePayload(accessToken, refreshToken);
    }

    @Override
    public RegisterResponsePayload register(UserCreationRequestPayload creationData) {
        if (userService.existsByEmail(creationData.getEmail())) {
            throw new RegistrationException("Registration exception: given already occupied email");
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
        //a bit redundant check needed for situation when user is deleted or email changed
        try {
            user = userService.findByEmail(
                    parsedRefreshToken
                            .claims()
                            .getSubject()
            );
        } catch (NotFoundException e) {
            throw new InvalidTokenException("Given invalid refresh token: no such logged in user");
        }

        log.info("User: " + user.getEmail() + " refreshed access token");
        return jwtService.issueAccessToken(user);
    }

    @Override
    public void logout(String userEmail) {
        jwtService.invalidateRefreshToken(userEmail);
        log.info("User: " + userEmail + " logged out");
    }
}
