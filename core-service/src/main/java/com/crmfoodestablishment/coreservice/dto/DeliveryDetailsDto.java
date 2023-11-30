package com.crmfoodestablishment.coreservice.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public class DeliveryDetailsDto {

    @NotNull(message = "First name must be written")
    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "Supporting utf-16 letters and characters : 1)'-' 2)'`' 3)'.' 4)' '"
    )
    @Size(
            max = 64,
            message = "Max length of first name is 64 characters"
    )
    private String firstName;

    @Pattern(
            regexp = "^[\\p{L} .'-]+$",
            message = "Supporting utf-16 letters and characters : 1)'-' 2)'`' 3)'.' 4)' '"
    )
    @Size(
            max = 64,
            message = "Max length of last name is 64 characters"
    )
    private String lastName;

    @Pattern(
            regexp = "(\\+?380|380)[\\s-]?\\(?(\\d{2})\\)?[\\s-]?\\d{2}[\\s-]?\\d{2}[\\s-]?\\d{3}",
            message = "Please inject a correct Ukrainian phone number format, started from 380")
    @Size(
            min = 12, max = 20,
            message = "Uncorrected number of digits, correct number start from 12 to 20"
    )
    private String phoneNumber;

    private String address;
}