package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.DishInOrder;
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

    private List<DishInOrder> listOfOrderDishes;

    @Length(max = 128)
    private String comment;

    private UUID userUuid;
}