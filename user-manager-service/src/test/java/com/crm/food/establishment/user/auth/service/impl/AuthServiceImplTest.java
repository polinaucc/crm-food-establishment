package com.crm.food.establishment.user.auth.service.impl;

import com.crm.food.establishment.user.auth.dto.CredentialsDto;
import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.exception.InvalidUserCredentialsException;
import com.crm.food.establishment.user.auth.service.JwtService;
import com.crm.food.establishment.user.auth.token.RefreshToken;
import com.crm.food.establishment.user.auth.token.RefreshTokenClaims;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.entity.User;
import com.crm.food.establishment.user.manager.repository.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    private CredentialsDto credentialsSample;
    private User userSample;
    private RefreshToken refreshTokenSample;
    private String refreshTokenAsString;

    @BeforeEach
    void setUp() {
        credentialsSample = new CredentialsDto(
                "test@gmail.com",
                "qwerty"
        );

        userSample = new User(UUID.randomUUID());
        userSample.setPassword("qwerty123");

        refreshTokenSample = new RefreshToken(
                null,
                new RefreshTokenClaims(
                        null,
                        null,
                        UUID.randomUUID()
                )
        );
        refreshTokenAsString = "refreshToken";
    }

    @Test
    void login_ShouldValidateEmail() {
        when(userRepository.findByEmail(credentialsSample.email())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(credentialsSample))
                .isInstanceOf(InvalidUserCredentialsException.class)
                .hasMessage("Wrong email");
        verify(userRepository).findByEmail(credentialsSample.email());
    }

    @Test
    void login_ShouldValidatePassword() {
        when(userRepository.findByEmail(credentialsSample.email())).thenReturn(Optional.of(userSample));
        when(passwordEncoder.matches(credentialsSample.password(), userSample.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(credentialsSample))
                .isInstanceOf(InvalidUserCredentialsException.class)
                .hasMessage("Wrong password");
        verify(userRepository).findByEmail(credentialsSample.email());
        verify(passwordEncoder).matches(credentialsSample.password(), userSample.getPassword());
    }

    @Test
    void login_ShouldReturnTokenPair() {
        TokenPair expectedTokenPair = new TokenPair("access","refresh");

        when(userRepository.findByEmail(credentialsSample.email())).thenReturn(Optional.of(userSample));
        when(passwordEncoder.matches(credentialsSample.password(), userSample.getPassword())).thenReturn(true);
        when(jwtService.issueTokenPair(userSample)).thenReturn(expectedTokenPair);

        TokenPair actualTokenPair = authService.login(credentialsSample);

        assertEquals(expectedTokenPair, actualTokenPair);
        verify(userRepository).findByEmail(credentialsSample.email());
        verify(passwordEncoder).matches(credentialsSample.password(), userSample.getPassword());
        verify(jwtService).issueTokenPair(userSample);
    }

    @Test
    void refresh_ShouldValidateRefreshTokenSubject() {
        when(jwtService.parseRefreshToken(refreshTokenAsString)).thenReturn(refreshTokenSample);
        when(userRepository.findByUuid(refreshTokenSample.claims().sub())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.refresh(refreshTokenAsString))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessage("Invalid subject: no users with id + " + refreshTokenSample.claims().sub());
        verify(jwtService).parseRefreshToken(refreshTokenAsString);
        verify(userRepository).findByUuid(refreshTokenSample.claims().sub());
    }

    @Test
    void refresh_ShouldReturnAccessToken() {
        String expectedAccessToken = "accessToken";

        when(jwtService.parseRefreshToken(refreshTokenAsString)).thenReturn(refreshTokenSample);
        when(userRepository.findByUuid(refreshTokenSample.claims().sub())).thenReturn(Optional.of(userSample));
        when(jwtService.issueAccessToken(userSample)).thenReturn(expectedAccessToken);

        String actualAccessToken = authService.refresh(refreshTokenAsString);

        assertEquals(expectedAccessToken, actualAccessToken);
        verify(jwtService).parseRefreshToken(refreshTokenAsString);
        verify(userRepository).findByUuid(refreshTokenSample.claims().sub());
        verify(jwtService).issueAccessToken(userSample);
    }

    @Test
    void logout_InvalidatesAssociatedRefreshToken() {
        UUID inputUuid = UUID.randomUUID();

        authService.logout(inputUuid);

        verify(jwtService).invalidateRefreshToken(inputUuid);
    }
}