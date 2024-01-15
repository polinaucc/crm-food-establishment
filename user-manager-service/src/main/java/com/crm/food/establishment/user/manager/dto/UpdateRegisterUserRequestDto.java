package com.crm.food.establishment.user.manager.dto;

import com.crm.food.establishment.user.manager.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_EMAIL_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_BLANK_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_NULL_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_PASSWORD_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.PAST_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.SIZE_MAX_1024_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.SIZE_MAX_32_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationRegexps.EMAIL_REGEXP;
import static com.crm.food.establishment.user.validation.ValidationRegexps.PASSWORD_REGEXP;

public record UpdateRegisterUserRequestDto(
        @Email(regexp = EMAIL_REGEXP, message = INVALID_EMAIL_MESSAGE)
        @NotBlank(message = NOT_BLANK_MESSAGE)
        String email,

        @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD_MESSAGE)
        @NotBlank(message = NOT_BLANK_MESSAGE)
        String password,

        @NotNull(message = NOT_NULL_MESSAGE)
        Role role,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Size(max = 32, message = SIZE_MAX_32_MESSAGE)
        String firstName,

        @NotBlank(message = NOT_BLANK_MESSAGE)
        @Size(max = 32, message = SIZE_MAX_32_MESSAGE)
        String lastName,

        boolean isMale,

        @NotNull(message = NOT_NULL_MESSAGE)
        @Past(message = PAST_MESSAGE)
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd"
        )
        LocalDate birthday,

        @Size(max = 1024, message = SIZE_MAX_1024_MESSAGE)
        String address
) {}
