package com.crm.food.establishment.user.validation;

import com.crm.food.establishment.user.ApiErrorDTO;
import com.crm.food.establishment.user.manager.controller.UserController;
import com.crm.food.establishment.user.manager.dto.UpdateRegisterUserRequestDto;
import com.crm.food.establishment.user.manager.exception.InvalidArgumentException;
import com.crm.food.establishment.user.manager.service.UserService;
import com.fasterxml.jackson.core.type.TypeReference;
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

import java.time.LocalDate;
import java.util.List;

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_EMAIL_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_NULL_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_PASSWORD_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_UUID_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(value = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class GlobalInputValidationControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    void handleMethodArgumentNotValid_ShouldComposeDTOList_And_ReturnBadRequestStatus() throws Exception {
        var invalidPayload = new UpdateRegisterUserRequestDto(
                "aaaaaa",
                "a",
                null,
                "John",
                "Dou",
                true,
                LocalDate.now().minusYears(2),
                "some"
        );
        List<ApiErrorDTO> expectedErrorDTOList = List.of(
                new ApiErrorDTO(InvalidArgumentException.errorCode(), "email: " + INVALID_EMAIL_MESSAGE),
                new ApiErrorDTO(InvalidArgumentException.errorCode(), "password: " + INVALID_PASSWORD_MESSAGE),
                new ApiErrorDTO(InvalidArgumentException.errorCode(), "role: " + NOT_NULL_MESSAGE)
        );

        ResultActions response = mockMvc.perform(
                post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidPayload))
        );
        List<ApiErrorDTO> actualErrorDTOList = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                new TypeReference<>() {}
        );

        response.andExpect(status().isBadRequest());
        assertThat(expectedErrorDTOList).hasSameElementsAs(actualErrorDTOList);
    }

    @Test
    void handleConstraintViolationException_ShouldComposeDTO_And_ReturnBadRequestStatus() throws Exception {
        ApiErrorDTO expectedErrorDTO = new ApiErrorDTO(InvalidArgumentException.errorCode(), INVALID_UUID_MESSAGE);

        ResultActions response = mockMvc.perform(get("/api/user/invalidUuid"));
        ApiErrorDTO actualErrorDTO = objectMapper.readValue(
                response.andReturn().getResponse().getContentAsString(),
                ApiErrorDTO.class
        );

        response.andExpect(status().isBadRequest());
        assertEquals(expectedErrorDTO, actualErrorDTO);
    }
}