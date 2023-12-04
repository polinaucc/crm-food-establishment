package com.crmfoodestablishment.usermanager.crud.controller;

import com.crmfoodestablishment.usermanager.crud.exception.InvalidArgumentException;
import com.crmfoodestablishment.usermanager.crud.exception.NotFoundException;
import com.crmfoodestablishment.usermanager.crud.service.UserService;

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
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @Test
    void handleInvalidArguments() throws Exception {
        InvalidArgumentException testException = new InvalidArgumentException("InvalidArgumentException");

        when(userService.listAll())
                .thenThrow(testException);

        ResultActions response = mockMvc.perform(
                get(UserController.USER_PATH)
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", is(testException.getMessage())));
    }

    @Test
    void handleNotFound() throws Exception {
        NotFoundException testException = new NotFoundException("NotFoundException");

        when(userService.listAll())
                .thenThrow(testException);

        ResultActions response = mockMvc.perform(
                get(UserController.USER_PATH)
        );

        response.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(NotFoundException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.description", is(testException.getMessage())));
    }
}