package com.crmfoodestablishment.coreservice.dto.order;

import com.crmfoodestablishment.coreservice.entity.DeliveryMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

import static com.crmfoodestablishment.coreservice.entity.DeliveryMethod.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NewOrderDto {

    @NotEmpty
    private UUID userUuid;

    @NotEmpty
    private List<DishInOrderDto> dishes;

    private String comment;

    private DeliveryMethod deliveryMethod;

    @Valid
    private DeliveryDetailsDto deliveryDetails;

    @Getter
    @Setter
    public static class DishInOrderDto {
        private UUID uuid;
        private Short amount;
    }

    public void validateDeliveryMethod(DeliveryDetailsDto deliveryDetails) {
        if (deliveryDetails.getPhoneNumber() == null) {
            deliveryMethod = LOCAL;
        }
        else if (deliveryDetails.getAddress() == null) {
            deliveryMethod = SELF_PICKUP;
        }
        else deliveryMethod = ADDRESS_DELIVERY;
    }
}