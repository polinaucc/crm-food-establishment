package com.crm.food.establishment.user.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import static com.crm.food.establishment.user.validation.ValidationErrorMessages.EMAIL_REGEXP_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.NOT_BLANC_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationErrorMessages.PASSWORD_REGEXP_MESSAGE;
import static com.crm.food.establishment.user.validation.ValidationRegexps.EMAIL_REGEXP;
import static com.crm.food.establishment.user.validation.ValidationRegexps.PASSWORD_REGEXP;

public record CredentialsDTO (
        @Email(regexp = EMAIL_REGEXP, message = EMAIL_REGEXP_MESSAGE)
        @NotBlank(message = NOT_BLANC_MESSAGE)
        String email,

        @Pattern(regexp = PASSWORD_REGEXP, message = PASSWORD_REGEXP_MESSAGE)
        @NotBlank(message = NOT_BLANC_MESSAGE)
        String password
) {}
