package com.crm.food.establishment.user.auth.service.impl;

import com.crm.food.establishment.user.manager.repository.UserRepository;
import com.crm.food.establishment.user.auth.dto.CredentialsDTO;
import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.exception.InvalidUserCredentialsException;
import com.crm.food.establishment.user.auth.service.AuthService;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.RefreshToken;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.entity.User;

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
                    if (!passwordEncoder.matches(
                            credentials.getPassword(),
                            foundUser.getPassword()
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
