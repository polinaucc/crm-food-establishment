package com.crmfoodestablishment.usermanager.auth.service.impl;

import com.crmfoodestablishment.usermanager.auth.dto.CredentialsDTO;
import com.crmfoodestablishment.usermanager.auth.exception.InvalidTokenException;
import com.crmfoodestablishment.usermanager.auth.exception.InvalidUserCredentialsException;
import com.crmfoodestablishment.usermanager.auth.service.JwtService;
import com.crmfoodestablishment.usermanager.auth.token.RefreshToken;
import com.crmfoodestablishment.usermanager.auth.token.RefreshTokenClaims;
import com.crmfoodestablishment.usermanager.auth.token.TokenPair;
import com.crmfoodestablishment.usermanager.crud.entity.User;
import com.crmfoodestablishment.usermanager.crud.repository.UserRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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

    @Test
    void login_ValidatesEmail() {
        CredentialsDTO credentials = new CredentialsDTO();
        credentials.setEmail("test@gmail.com");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidUserCredentialsException.class,
                () -> authService.login(credentials)
        );
        verify(userRepository, times(1))
                .findByEmail(credentials.getEmail());
    }

    @Test
    void login_ValidatesPassword() {
        CredentialsDTO credentials = new CredentialsDTO();
        credentials.setEmail("test@gmail.com");
        credentials.setPassword("qwerty");
        User user = new User();
        user.setPassword("qwerty1");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(false);

        assertThrows(
                InvalidUserCredentialsException.class,
                () -> authService.login(credentials)
        );
        verify(userRepository, times(1))
                .findByEmail(credentials.getEmail());
        verify(passwordEncoder, times(1))
                .matches(
                        credentials.getPassword(),
                        user.getPassword()
                );
    }

    @Test
    void login_ReturnTokenPair() {
        CredentialsDTO credentials = new CredentialsDTO();
        credentials.setEmail("test@gmail.com");
        credentials.setPassword("qwerty");
        User user = new User();
        user.setPassword("qwerty1");

        when(userRepository.findByEmail(any()))
                .thenReturn(Optional.of(user));
        when(passwordEncoder.matches(any(), any()))
                .thenReturn(true);
        when(jwtService.issueTokenPair(any()))
                .thenReturn(new TokenPair("", ""));

        TokenPair tokenPair = authService.login(credentials);

        assertNotNull(tokenPair);
        assertEquals("", tokenPair.getAccessToken());
        assertEquals("", tokenPair.getRefreshToken());
        verify(userRepository, times(1))
                .findByEmail(credentials.getEmail());
        verify(passwordEncoder, times(1))
                .matches(
                        credentials.getPassword(),
                        user.getPassword()
                );
        verify(jwtService, times(1))
                .issueTokenPair(user);
    }

    @Test
    void refresh_ValidatesRefreshTokenSubject() {
        RefreshToken refreshToken = new RefreshToken(
                null,
                new RefreshTokenClaims(
                        null,
                        null,
                        UUID.randomUUID()
                )
        );

        when(jwtService.parseRefreshToken(any()))
                .thenReturn(refreshToken);
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.empty());

        assertThrows(
                InvalidTokenException.class,
                () -> authService.refresh("refreshToken")
        );
        verify(jwtService, times(1))
                .parseRefreshToken("refreshToken");
        verify(userRepository, times(1))
                .findByUuid(refreshToken.claims().sub());
    }

    @Test
    void refresh_ReturnsAccessToken() {
        RefreshToken refreshToken = new RefreshToken(
                null,
                new RefreshTokenClaims(
                        null,
                        null,
                        UUID.randomUUID()
                )
        );
        User user = new User(UUID.randomUUID());

        when(jwtService.parseRefreshToken(any()))
                .thenReturn(refreshToken);
        when(userRepository.findByUuid(any()))
                .thenReturn(Optional.of(user));
        when(jwtService.issueAccessToken(user))
                .thenReturn("accessToken");

        String accessToken = authService.refresh("refreshToken");

        assertEquals("accessToken", accessToken);
        verify(jwtService, times(1))
                .parseRefreshToken("refreshToken");
        verify(userRepository, times(1))
                .findByUuid(refreshToken.claims().sub());
        verify(jwtService, times(1))
                .issueAccessToken(user);
    }

    @Test
    void logout_InvalidatesAssociatedRefreshToken() {
        UUID testUuid = UUID.randomUUID();

        authService.logout(testUuid);

        verify(jwtService, times(1))
                .invalidateRefreshToken(testUuid);
    }
}