package com.crmfoodestablishment.userauthservice.usermanager.dto;

import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

//TODO write validation messages
@Getter
@Setter
public class RegisterUserRequestDTO {

    @NotNull
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
    private String email;

    @NotBlank
    private String password;

    @NotNull
    private Role role;

    @NotBlank
    @Size(max = 32)
    private String firstName;

    @NotBlank
    @Size(max = 32)
    private String lastName;

    @NotNull
    private Boolean isMale;

    @NotNull
    @Past
    @JsonFormat(
            shape = JsonFormat.Shape.STRING,
            pattern = "yyyy-MM-dd"
    )
    private LocalDate birthday;

    @Size(max = 1024)
    private String address;
}
