package com.crmfoodestablishment.usermanager.auth.controller;

import com.crmfoodestablishment.usermanager.auth.dto.CredentialsDTO;
import com.crmfoodestablishment.usermanager.auth.service.AuthService;
import com.crmfoodestablishment.usermanager.auth.token.TokenPair;
import com.crmfoodestablishment.usermanager.crud.exception.InvalidArgumentException;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Captor
    ArgumentCaptor<CredentialsDTO> credentialsCaptor;

    @Test
    void login_ValidatesCredentialsDTO() throws Exception {
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setEmail("email");
        credentialsDTO.setPassword(null);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                credentialsDTO
                        ))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void login_ReturnsTokenPair_And_OkStatus() throws Exception {
        CredentialsDTO credentialsDTO = new CredentialsDTO();
        credentialsDTO.setEmail("test@gmail.com");
        credentialsDTO.setPassword("qwerty");
        TokenPair tokenPair = new TokenPair(
                "accessToken",
                "refreshToken"
        );

        when(authService.login(any()))
                .thenReturn(tokenPair);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                credentialsDTO
                        ))
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.accessToken", is(tokenPair.getAccessToken())))
                .andExpect(jsonPath("$.refreshToken", is(tokenPair.getRefreshToken())));
        verify(authService).login(credentialsCaptor.capture());
        assertEquals(credentialsDTO.getEmail(), credentialsCaptor.getValue().getEmail());
        assertEquals(credentialsDTO.getPassword(), credentialsCaptor.getValue().getPassword());
    }

    @Test
    void refresh_ValidatesRefreshToken() throws Exception {
        String refreshToken = "invalid_token";

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/refresh")
                        .queryParam("refreshToken", refreshToken)
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void refresh_ReturnsAccessToken_And_OkStatus() throws Exception {
        String refreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

        when(authService.refresh(any()))
                .thenReturn("accessToken");

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/refresh")
                        .queryParam("refreshToken", refreshToken)
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string("accessToken"));
        verify(authService, times(1))
                .refresh(refreshToken);
    }

    @Test
    void logout_ValidatesUserUuid() throws Exception {
        String testUuid = "invalidUuid";

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/logout/" + testUuid)
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void logout_ValidatesNilUserUuid() throws Exception {
        String testUuid = "00000000-0000-0000-0000-000000000000";

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/logout/" + testUuid)
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void logout_ReturnsOkStatus() throws Exception {
        UUID testUuid = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/logout/" + testUuid)
        );

        response.andExpect(status().isOk());
        verify(authService, times(1))
                .logout(testUuid);
    }
}