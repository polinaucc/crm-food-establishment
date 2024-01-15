package com.crm.food.establishment.user.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_EMAIL_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_BLANK_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.INVALID_PASSWORD_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationRegexps.EMAIL_REGEXP;
import static com.crm.food.establishment.user.validation.ValidationRegexps.PASSWORD_REGEXP;

public record CredentialsDto(
        @Email(regexp = EMAIL_REGEXP, message = INVALID_EMAIL_MESSAGE)
        @NotBlank(message = NOT_BLANK_MESSAGE)
        String email,

        @Pattern(regexp = PASSWORD_REGEXP, message = INVALID_PASSWORD_MESSAGE)
        @NotBlank(message = NOT_BLANK_MESSAGE)
        String password
) {}
