package com.crm.food.establishment.user.auth.controller;

import com.crm.food.establishment.user.auth.dto.CredentialsDTO;
import com.crm.food.establishment.user.auth.service.AuthService;
import com.crm.food.establishment.user.auth.token.TokenPair;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Test
    void login_ShouldValidateCredentialsDTO() throws Exception {
        CredentialsDTO invalidCredentials = new CredentialsDTO("email", null);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidCredentials))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size()", is(2)));
        verifyNoInteractions(authService);
    }

    @Test
    void login_ShouldReturnTokenPair_And_OkStatus() throws Exception {
        CredentialsDTO inputCredentials = new CredentialsDTO("test@gmail.com", "qwerty1234");
        TokenPair expectedTokenPair = new TokenPair("accessToken", "refreshToken");
        when(authService.login(inputCredentials)).thenReturn(expectedTokenPair);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputCredentials))
        );
        TokenPair actualTokenPair = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                TokenPair.class
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedTokenPair, actualTokenPair);
        verify(authService).login(inputCredentials);
    }

    @Test
    void refresh_ShouldValidateRefreshToken() throws Exception {
        String invalidRefreshToken = "invalid_token";

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_REFRESH_PATH).queryParam("refreshToken", invalidRefreshToken)
        );

        response.andExpect(status().isBadRequest());
        verifyNoInteractions(authService);
    }

    @Test
    void refresh_ShouldReturnAccessToken_And_OkStatus() throws Exception {
        String inputRefreshToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";
        String expectedAccessToken = "accessToken";
        when(authService.refresh(inputRefreshToken)).thenReturn(expectedAccessToken);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_REFRESH_PATH).queryParam("refreshToken", inputRefreshToken)
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("text/plain;charset=UTF-8")))
                .andExpect(content().string(expectedAccessToken));
        verify(authService).refresh(inputRefreshToken);
    }

    @Test
    void logout_ShouldValidateUserId() throws Exception {
        String invalidUuid = "invalidUuid";

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_LOGOUT_PATH.replace("{userId}", invalidUuid))
        );

        response.andExpect(status().isBadRequest());
        verifyNoInteractions(authService);
    }

    @Test
    void logout_ShouldCallService_And_ReturnOkStatus() throws Exception {
        UUID inputUuid = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_LOGOUT_PATH.replace("{userId}", inputUuid.toString()))
        );

        response.andExpect(status().isOk());
        verify(authService).logout(inputUuid);
    }
}