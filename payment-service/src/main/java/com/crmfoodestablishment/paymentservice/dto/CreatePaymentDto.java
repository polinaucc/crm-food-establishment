package com.crmfoodestablishment.paymentservice.dto;

import com.crmfoodestablishment.paymentservice.entity.Status;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CreatePaymentDto {

    private UUID uuid;

    @NotNull(message = "isCash cannot be null")
    private Boolean isCash;

    @Positive(message = "sum cannot be zero or negative")
    @NotNull(message = "sum cannot be null")
    private BigDecimal sum;

    private LocalDateTime paymentDate;

    @NotNull(message = "status cannot be null")
    private Status status;
}
