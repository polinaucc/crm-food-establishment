package com.crm.food.establishment.user.manager.controller;

import com.crm.food.establishment.user.auth.token.TokenPair;
import com.crm.food.establishment.user.manager.dto.RegisterUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.RegisterUserResponseDTO;
import com.crm.food.establishment.user.manager.dto.UpdateUserRequestDTO;
import com.crm.food.establishment.user.manager.dto.UserDTO;
import com.crm.food.establishment.user.manager.entity.Role;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.exception.NotFoundException;
import com.crm.food.establishment.user.manager.service.UserService;

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

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private RegisterUserRequestDTO validRegisterPayload;
    private RegisterUserRequestDTO invalidRegisterPayload;
    private UpdateUserRequestDTO validUpdatePayload;
    private UpdateUserRequestDTO invalidUpdatePayload;

    @BeforeEach
    public void setUpTestData() {
        validRegisterPayload = new RegisterUserRequestDTO();
        validRegisterPayload.setEmail("test@gmail.com");
        validRegisterPayload.setPassword("qwerty");
        validRegisterPayload.setRole(Role.CLIENT);
        validRegisterPayload.setFirstName("John");
        validRegisterPayload.setLastName("Dou");
        validRegisterPayload.setIsMale(true);
        validRegisterPayload.setBirthday(LocalDate.now().minusYears(2));
        validRegisterPayload.setAddress("Some address");

        invalidRegisterPayload = new RegisterUserRequestDTO();
        invalidRegisterPayload.setEmail("aaaaaaa");
        invalidRegisterPayload.setPassword("");
        invalidRegisterPayload.setRole(null);
        invalidRegisterPayload.setFirstName("");
        invalidRegisterPayload.setLastName("");
        invalidRegisterPayload.setIsMale(null);
        invalidRegisterPayload.setBirthday(LocalDate.now().plusYears(2));

        validUpdatePayload = mapRegisterUserRequestDTOToUpdateUserRequestDTO(
                validRegisterPayload
        );
        invalidUpdatePayload = mapRegisterUserRequestDTOToUpdateUserRequestDTO(
                invalidRegisterPayload
        );
    }

    @Test
    public void registerUser_ValidatesInput() throws Exception {
        ResultActions response = mockMvc.perform(
                post(UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                invalidRegisterPayload
                        ))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    public void registerUser_ReturnsRegisterUserResponseDTO() throws Exception {
        var registerUserResponseDTO = new RegisterUserResponseDTO(
                UUID.randomUUID(),
                new TokenPair("", "")
        );

        when(userService.register(any()))
                .thenReturn(registerUserResponseDTO);

        ResultActions response = mockMvc.perform(
                post(UserController.USER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRegisterPayload))
        );

        response.andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.userUuid", is(
                        registerUserResponseDTO.getUserUuid().toString()
                )))
                .andExpect(jsonPath("$.tokenPair", notNullValue()));
    }

    @Test
    void updateUser_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                put(UserController.USER_PATH + "/" + "invalid_UUID")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdatePayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void updateUser_ValidatesInputData() throws Exception {
        ResultActions response = mockMvc.perform(
                put(UserController.USER_PATH + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUpdatePayload))
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void updateUser_ReturnsNoContent() throws Exception {
        ResultActions response = mockMvc.perform(
                put(UserController.USER_PATH + "/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validUpdatePayload))
        );

        response.andExpect(status().isNoContent());
    }

    @Test
    void deleteUser_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                delete(UserController.USER_PATH + "/" + "invalid_UUID")
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void deleteUser_ReturnsNoContent() throws Exception {
        ResultActions response = mockMvc.perform(
                delete(UserController.USER_PATH + "/" + UUID.randomUUID())
        );

        response.andExpect(status().isNoContent());
    }

    @Test
    void getUserById_ValidatesPathVariable() throws Exception {
        ResultActions response = mockMvc.perform(
                get(UserController.USER_PATH + "/" + "invalid_UUID")
        );

        response.andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(InvalidArgumentException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.BAD_REQUEST.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    @Test
    void getUserById_ReturnsUserDTO() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(UUID.randomUUID());

        when(userService.getById(any()))
                .thenReturn(userDTO);

        ResultActions response = mockMvc.perform(
                get(UserController.USER_PATH + "/" + userDTO.getUuid())
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid", is(
                        userDTO.getUuid().toString()
                )));
    }

    @Test
    void listUsers() throws Exception {
        UserDTO userDTO = new UserDTO();
        userDTO.setUuid(UUID.randomUUID());

        when(userService.listAll())
                .thenReturn(List.of(userDTO, userDTO));

        ResultActions response = mockMvc.perform(
                get(UserController.USER_PATH)
        );

        response.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    void correctlyHandlesNotFoundException() throws Exception {
        when(userService.getById(any())).thenThrow(new NotFoundException("some message"));

        ResultActions response = mockMvc.perform(
                get(UserController.USER_PATH + "/" + UUID.randomUUID())
        );

        response.andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title", is(NotFoundException.readableName())))
                .andExpect(jsonPath("$.status", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.description", notNullValue()));
    }

    private UpdateUserRequestDTO mapRegisterUserRequestDTOToUpdateUserRequestDTO(
            RegisterUserRequestDTO registerUserRequestDTO
    ) {
        var responseDTO = new UpdateUserRequestDTO();
        responseDTO.setEmail(registerUserRequestDTO.getEmail());
        responseDTO.setPassword(registerUserRequestDTO.getPassword());
        responseDTO.setRole(registerUserRequestDTO.getRole());
        responseDTO.setFirstName(registerUserRequestDTO.getFirstName());
        responseDTO.setLastName(registerUserRequestDTO.getLastName());
        responseDTO.setIsMale(registerUserRequestDTO.getIsMale());
        responseDTO.setBirthday(registerUserRequestDTO.getBirthday());
        responseDTO.setAddress(registerUserRequestDTO.getAddress());
        return responseDTO;
    }
}