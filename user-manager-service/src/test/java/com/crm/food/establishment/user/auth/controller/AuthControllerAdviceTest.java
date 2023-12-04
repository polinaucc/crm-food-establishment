package com.crm.food.establishment.user.auth.controller;

import com.crm.food.establishment.user.auth.dto.CredentialsDTO;
import com.crm.food.establishment.user.auth.exception.InvalidTokenException;
import com.crm.food.establishment.user.auth.exception.InvalidUserCredentialsException;
import com.crm.food.establishment.user.auth.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class AuthControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    private CredentialsDTO credentialsDTO;

    @BeforeEach
    void setUp() {
        credentialsDTO = new CredentialsDTO();
        credentialsDTO.setEmail("test@gmail.com");
        credentialsDTO.setPassword("qwerty");
    }

    @Test
    void handleInvalidToken() throws Exception {
        InvalidTokenException testException = new InvalidTokenException("InvalidTokenException");

        when(authService.login(any()))
                .thenThrow(testException);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                credentialsDTO
                        ))
        );

        response.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidTokenException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.UNAUTHORIZED.name())))
                .andExpect(jsonPath("$.description", is(testException.getMessage())));
    }

    @Test
    void handleInvalidUserCredentials() throws Exception {
        InvalidUserCredentialsException testException = new InvalidUserCredentialsException("InvalidUserCredentialsException");

        when(authService.login(any()))
                .thenThrow(testException);

        ResultActions response = mockMvc.perform(
                post(AuthController.AUTH_PATH + "/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                credentialsDTO
                        ))
        );

        response.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidUserCredentialsException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.UNAUTHORIZED.name())))
                .andExpect(jsonPath("$.description", is(testException.getMessage())));
    }
}