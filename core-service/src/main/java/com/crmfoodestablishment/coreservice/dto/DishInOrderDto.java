package com.crmfoodestablishment.coreservice.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
public class DishInOrderDto {

        private UUID uuid;
        private Short amount;
}
