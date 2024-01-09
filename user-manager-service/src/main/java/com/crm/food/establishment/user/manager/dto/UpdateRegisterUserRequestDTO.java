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

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.EMAIL_REGEXP_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_BLANC_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_NULL_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.PASSWORD_REGEXP_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.PAST_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.SIZE_MAX_1024_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.SIZE_MAX_32_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationRegexps.EMAIL_REGEXP;
import static com.crm.food.establishment.user.validation.ValidationRegexps.PASSWORD_REGEXP;

public record UpdateRegisterUserRequestDTO(
        @Email(regexp = EMAIL_REGEXP, message = EMAIL_REGEXP_MESSAGE)
        @NotBlank(message = NOT_BLANC_MESSAGE)
        String email,

        @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_REGEXP_MESSAGE)
        @NotBlank(message = NOT_BLANC_MESSAGE)
        String password,

        @NotNull(message = NOT_NULL_MESSAGE)
        Role role,

        @NotBlank(message = NOT_BLANC_MESSAGE)
        @Size(max = 32, message = SIZE_MAX_32_MESSAGE)
        String firstName,

        @NotBlank(message = NOT_BLANC_MESSAGE)
        @Size(max = 32, message = SIZE_MAX_32_MESSAGE)
        String lastName,

        @NotNull(message = NOT_NULL_MESSAGE)
        Boolean isMale,

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
