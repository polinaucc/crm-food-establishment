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

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public TokenPair login(CredentialsDTO credentials) {
        User foundUser = userRepository
                .findByEmail(credentials.email())
                .orElseThrow(() -> new InvalidUserCredentialsException("Wrong email"));
        if (!passwordEncoder.matches(credentials.password(), foundUser.getPassword())) {
            throw new InvalidUserCredentialsException("Wrong password");
        }

        log.info("User: " + foundUser.getEmail() + " logged");
        return jwtService.issueTokenPair(foundUser);
    }

    //TODO implement maximum number of access tokens for one user
    @Override
    public String refresh(String refreshToken) {
        RefreshToken parsedRefreshToken = jwtService.parseRefreshToken(refreshToken);

        User foundUser = userRepository
                .findByUuid(parsedRefreshToken.claims().sub())
                .orElseThrow(() -> new InvalidTokenException(
                        "Invalid subject: no users with id + " + parsedRefreshToken.claims().sub()
                ));

        log.info("User: " + foundUser.getUuid() + " refreshed access token");
        return jwtService.issueAccessToken(foundUser);
    }

    @Override
    public void logout(UUID userUuid) {
        jwtService.invalidateRefreshToken(userUuid);
        log.info("User: " + userUuid + " logged out");
    }
}
