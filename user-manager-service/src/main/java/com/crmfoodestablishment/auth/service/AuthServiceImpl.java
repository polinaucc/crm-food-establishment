package com.crmfoodestablishment.auth.service;

import com.crmfoodestablishment.auth.controller.payload.LoginRequestPayload;
import com.crmfoodestablishment.auth.controller.payload.RegisterResponsePayload;
import com.crmfoodestablishment.auth.controller.payload.TokenPairResponsePayload;
import com.crmfoodestablishment.auth.controller.payload.UserCreationRequestPayload;
import com.crmfoodestablishment.auth.exception.InvalidTokenException;
import com.crmfoodestablishment.usermanager.exception.NotFoundException;
import com.crmfoodestablishment.auth.exception.WrongUserCredentialsException;
import com.crmfoodestablishment.auth.service.model.RefreshToken;
import com.crmfoodestablishment.usermanager.entity.User;
import com.crmfoodestablishment.usermanager.services.UserService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserService userService;

    @Override
    public TokenPairResponsePayload login(LoginRequestPayload credentials) {
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

        return new TokenPairResponsePayload(accessToken, refreshToken);
    }

    @Override
    public RegisterResponsePayload register(UserCreationRequestPayload creationData) {
        User createdUser = userService.createUser(creationData);

        String accessToken = jwtService.issueAccessToken(createdUser);
        String refreshToken = jwtService.issueRefreshToken(createdUser);

        String createdUserUrl = USER_CRUD_PATH + "/" + createdUser.getId();

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
            user = userService.findByEmail(
                    parsedRefreshToken
                            .claims()
                            .getSubject()
            );
        } catch (NotFoundException e) {
            throw new InvalidTokenException("Given invalid refresh token: no such logged in user");
        }

        return jwtService.issueAccessToken(user);
    }

    @Override
    public void logout(String userEmail) {
        jwtService.invalidateRefreshToken(userEmail);
    }
}
