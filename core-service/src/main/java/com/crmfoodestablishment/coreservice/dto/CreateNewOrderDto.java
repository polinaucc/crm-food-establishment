package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.DeliveryMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CreateNewOrderDto {

    @NotNull(message = "Uuid field cannot be empty")
    @Pattern(
            regexp = "^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$",
            message = "Invalid uuid"
    )
    private String userUuid;

    @NotEmpty(message = "List dishes of order cannot be a empty")
    private List<DishInOrderDto> dishes;

    private String comment;

    @NotNull(message = "Delivery method cannot be a null")
    private DeliveryMethod deliveryMethod;

    @NotNull(message = "Delivery details cannot be a null, please set first name at least")
    @Valid
    private DeliveryDetailsDto deliveryDetails;
}
