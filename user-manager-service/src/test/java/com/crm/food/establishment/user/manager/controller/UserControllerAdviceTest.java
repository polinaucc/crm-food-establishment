package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.ApiErrorDTO;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;
import com.crm.food.establishment.user.manager.service.UserService;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    public static final String USER_PATH = "/api/user";

    @Test
    void handleInvalidArguments_ShouldComposeDTO_And_ReturnBadRequestStatus() throws Exception {
        InvalidArgumentException expectedException = new InvalidArgumentException("InvalidArgumentException");
        ApiErrorDTO expectedErrorDTO = new ApiErrorDTO(
                InvalidArgumentException.errorCode(),
                expectedException.getMessage()
        );
        when(userService.getAllUsers()).thenThrow(expectedException);

        ResultActions response = mockMvc.perform(get(USER_PATH));
        ApiErrorDTO actualErrorDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                ApiErrorDTO.class
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedErrorDTO, actualErrorDTO);
    }

    @Test
    void handleNotFound_ShouldComposeDTO_And_ReturnNotFoundStatus() throws Exception {
        NotFoundException expectedException = new NotFoundException("NotFoundException");
        ApiErrorDTO expectedErrorDTO = new ApiErrorDTO(
                NotFoundException.errorCode(),
                expectedException.getMessage()
        );
        when(userService.getAllUsers()).thenThrow(expectedException);

        ResultActions response = mockMvc.perform(get(USER_PATH));
        ApiErrorDTO actualErrorDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                ApiErrorDTO.class
        );

        response.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedErrorDTO, actualErrorDTO);
    }
}