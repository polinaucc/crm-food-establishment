package com.crm.food.establishment.user.manager.dto;

import com.crm.food.establishment.user.manager.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public record UpdateUserRequestDTO (
        @NotNull
        @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,

        @NotBlank
        String password,

        @NotNull
        Role role,

        @NotBlank
        @Size(max = 32)
        String firstName,

        @NotBlank
        @Size(max = 32)
        String lastName,

        @NotNull
        Boolean isMale,

        @NotNull
        @Past
        @JsonFormat(
                shape = JsonFormat.Shape.STRING,
                pattern = "yyyy-MM-dd"
        )
        LocalDate birthday,

        @Size(max = 1024)
        String address
) {}
