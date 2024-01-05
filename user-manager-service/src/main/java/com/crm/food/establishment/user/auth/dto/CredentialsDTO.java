package com.crm.food.establishment.user.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CredentialsDTO (
        @Email
        @NotBlank
        String email,

        @NotBlank
        String password
) {}
