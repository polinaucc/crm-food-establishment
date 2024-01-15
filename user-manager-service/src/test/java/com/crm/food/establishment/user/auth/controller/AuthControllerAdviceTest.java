package com.crm.food.establishment.user.auth.controller;

import com.crm.food.establishment.user.ApiErrorDTO;
import com.crm.food.establishment.user.auth.dto.CredentialsDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    public static final String AUTH_LOGIN_PATH = "/api/auth/login";
    private CredentialsDto credentialsSample;

    @BeforeEach
    void setUp() {
        credentialsSample = new CredentialsDto("test@gmail.com", "qwerty1234");
    }

    @Test
    void handleInvalidToken_ShouldComposeDTO_And_ReturnUnauthorizedStatus() throws Exception {
        InvalidTokenException expectedException = new InvalidTokenException("InvalidTokenException");
        ApiErrorDTO expectedErrorDTO = new ApiErrorDTO(
                InvalidTokenException.errorCode(),
                expectedException.getMessage()
        );
        when(authService.login(any())).thenThrow(expectedException);

        ResultActions response = mockMvc.perform(
                post(AUTH_LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsSample))
        );
        ApiErrorDTO actualErrorDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                ApiErrorDTO.class
        );

        response.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedErrorDTO, actualErrorDTO);
    }

    @Test
    void handleInvalidUserCredentials_ShouldComposeDTO_And_ReturnUnauthorizedStatus() throws Exception {
        InvalidUserCredentialsException expectedException = new InvalidUserCredentialsException("InvalidUserCredentialsException");
        ApiErrorDTO expectedErrorDTO = new ApiErrorDTO(
                InvalidUserCredentialsException.errorCode(),
                expectedException.getMessage()
        );
        when(authService.login(any())).thenThrow(expectedException);

        ResultActions response = mockMvc.perform(
                post(AUTH_LOGIN_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(credentialsSample))
        );
        ApiErrorDTO actualErrorDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                ApiErrorDTO.class
        );

        response.andExpect(status().isUnauthorized())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedErrorDTO, actualErrorDTO);
    }
}