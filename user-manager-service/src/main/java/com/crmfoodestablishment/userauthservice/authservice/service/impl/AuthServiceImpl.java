package com.crmfoodestablishment.userauthservice.authservice.service.impl;

import com.crmfoodestablishment.userauthservice.authservice.dto.CredentialsDTO;
import com.crmfoodestablishment.userauthservice.authservice.exception.InvalidTokenException;
import com.crmfoodestablishment.userauthservice.authservice.exception.InvalidUserCredentialsException;
import com.crmfoodestablishment.userauthservice.authservice.service.AuthService;
import com.crmfoodestablishment.userauthservice.authservice.service.JwtService;
import com.crmfoodestablishment.userauthservice.authservice.token.RefreshToken;
import com.crmfoodestablishment.userauthservice.authservice.token.TokenPair;
import com.crmfoodestablishment.userauthservice.usermanager.entity.User;
import com.crmfoodestablishment.userauthservice.usermanager.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenPair login(CredentialsDTO credentials) {
        AtomicReference<User> user = new AtomicReference<>();

        userRepository.findByEmail(credentials.getEmail()).ifPresentOrElse(
                foundUser -> {
                    if (!foundUser.getPassword().equals(
                            passwordEncoder.encode(credentials.getPassword())
                    )) {
                        logAndThrowException(
                                new InvalidUserCredentialsException("Wrong password")
                        );
                    } else {
                        user.set(foundUser);
                    }
                },
                () -> logAndThrowException(
                        new InvalidUserCredentialsException("Wrong email")
                )
        );

        TokenPair tokenPair = jwtService.issueTokenPair(user.get());

        log.info("User: " + user.get().getEmail() + " logged");
        return tokenPair;
    }

    @Override
    public String refresh(String refreshToken) {
        //TODO implement maximum number of access tokens for one user

        RefreshToken parsedRefreshToken = jwtService.parseRefreshToken(refreshToken);

        AtomicReference<User> user = new AtomicReference<>();
        userRepository.findByUuid(parsedRefreshToken.claims().sub()).ifPresentOrElse(
                user::set,
                () -> logAndThrowException(
                        new InvalidTokenException("Invalid subject: no such user with that id")
                )
        );

        log.info("User: " + user.get().getUuid() + " refreshed access token");
        return jwtService.issueAccessToken(user.get());
    }

    @Override
    public void logout(UUID userUuid) {
        jwtService.invalidateRefreshToken(userUuid);
        log.info("User: " + userUuid + " logged out");
    }

    private void logAndThrowException(RuntimeException exception) {
        log.error(exception.getMessage(), exception);
        throw exception;
    }
}
