package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDto;
import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.dto.UserDto;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    public static final String USER_PATH = "/api/user";
    public static final String USER_PATH_WITH_ID = "/api/user/{userId}";
    private UpdateRegisterUserRequestDto validUpdateRegisterPayload;
    private UpdateRegisterUserRequestDto invalidUpdateRegisterPayload;
    private UserDto userDTOSample;

    @BeforeEach
    public void setUp() {
        validUpdateRegisterPayload = new UpdateRegisterUserRequestDto(
                "test@gmail.com",
                "qwerty1234",
                Role.CLIENT,
                "John",
                "Dou",
                true,
                LocalDate.now().minusYears(2),
                "Some address"
        );
        invalidUpdateRegisterPayload = new UpdateRegisterUserRequestDto(
                "aaaaaaa",
                "",
                null,
                "",
                "",
                true,
                LocalDate.now().plusYears(2),
                null
        );
        userDTOSample = new UserDto(
                UUID.randomUUID(),
                "test@gmail.com",
                Role.CLIENT,
                "John",
                "Dou",
                true,
                LocalDate.now().minusYears(2),
                "Some address"
        );
    }

    @Test
    public void registerUser_ShouldValidateRequestBody() throws Exception {
        ResultActions response = mockMvc.perform(
                post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateRegisterPayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size()", is(7)));
        verifyNoInteractions(userService);
    }

    @Test
    public void registerUser_ShouldReturnRegisterUserResponseDTO_And_CreatedStatus() throws Exception {
        var expectedRegisterUserResponseDTO = new RegisterUserResponseDto(
                UUID.randomUUID(), new TokenPair("", "")
        );
        when(userService.registerUser(validUpdateRegisterPayload)).thenReturn(expectedRegisterUserResponseDTO);

        ResultActions response = mockMvc.perform(
                post(USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRegisterPayload))
        );
        var actualRegisterUserResponseDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                RegisterUserResponseDto.class
        );

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(expectedRegisterUserResponseDTO, actualRegisterUserResponseDTO);
    }

    @Test
    void updateUser_ShouldValidateUserId() throws Exception {
        ResultActions response = mockMvc.perform(
                put(USER_PATH_WITH_ID.replace("{userId}", "invalidUuid"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRegisterPayload))
        );

        response.andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void updateUser_ShouldValidateRequestBody() throws Exception {
        ResultActions response = mockMvc.perform(
                put(USER_PATH_WITH_ID.replace("{userId}", UUID.randomUUID().toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdateRegisterPayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.size()", is(7)));
        verifyNoInteractions(userService);
    }

    @Test
    void updateUser_ShouldCallService_And_ReturnNoContent() throws Exception {
        UUID inputUserId = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                put(USER_PATH_WITH_ID.replace("{userId}", inputUserId.toString()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdateRegisterPayload))
        );

        response.andExpect(status().isNoContent());
        verify(userService).updateUser(inputUserId, validUpdateRegisterPayload);
    }

    @Test
    void deleteUser_ShouldValidateUserId() throws Exception {
        ResultActions response = mockMvc.perform(
                delete(USER_PATH_WITH_ID.replace("{userId}", "invalidUuid"))
        );

        response.andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void deleteUser_ShouldCallService_And_ReturnsNoContent() throws Exception {
        UUID inputUserId = UUID.randomUUID();

        ResultActions response = mockMvc.perform(
                delete(USER_PATH_WITH_ID.replace("{userId}", inputUserId.toString()))
        );

        response.andExpect(status().isNoContent());
        verify(userService).deleteUser(inputUserId);
    }

    @Test
    void getUserById_ShouldValidateUserId() throws Exception {
        ResultActions response = mockMvc.perform(
                get(USER_PATH_WITH_ID.replace("{userId}", "invalidUuid"))
        );

        response.andExpect(status().isBadRequest());
        verifyNoInteractions(userService);
    }

    @Test
    void getUserById_ShouldReturnUserDTO_And_OkStatus() throws Exception {
        when(userService.getUserById(userDTOSample.uuid())).thenReturn(userDTOSample);

        ResultActions response = mockMvc.perform(
                get(USER_PATH_WITH_ID.replace("{userId}", userDTOSample.uuid().toString()))
        );
        var actualUserDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                UserDto.class
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertEquals(userDTOSample, actualUserDTO);
        verify(userService).getUserById(userDTOSample.uuid());
    }

    @Test
    void listUsers_ShouldReturnUserDTOList_And_OkStatus() throws Exception {
        List<UserDto> expectedUserDTOList = List.of(userDTOSample, userDTOSample);
        when(userService.getAllUsers()).thenReturn(expectedUserDTOList);

        ResultActions response = mockMvc.perform(get(USER_PATH));
        List<UserDto> actualUserDTOList = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        assertIterableEquals(expectedUserDTOList, actualUserDTOList);
    }
}