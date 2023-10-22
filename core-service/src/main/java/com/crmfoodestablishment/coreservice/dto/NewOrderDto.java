package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderDto {

    private UUID userUuid;

    @NotEmpty
    private List<DishInOrderDto> dishes;

    private String comment;

    @NotEmpty
    private DeliveryMethod deliveryMethod;

    private DeliveryDetailsDto deliveryDetails; //TODO: needs validation: if address -> deliveryDetails, if pickup, local -> delivDetails = null

    @Getter
    @Setter
    public static class DishInOrderDto {
        private UUID uuid;
        private Short amount;
    }
}