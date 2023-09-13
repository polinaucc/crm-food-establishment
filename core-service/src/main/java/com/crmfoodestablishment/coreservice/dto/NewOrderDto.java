package com.crmfoodestablishment.coreservice.dto;

import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
public class NewOrderDto {

    private List<DishInOrder> dishInOrderList = new ArrayList<>();

    @Length(max = 128)
    private String comment;

    private UUID userUuid;

    public NewOrderDto() {}

    public NewOrderDto(List<DishInOrder> dishInOrderList, String comment, UUID userUuid) {
        this.dishInOrderList = dishInOrderList;
        this.comment = comment;
        this.userUuid = userUuid;
    }

    public NewOrderDto(NewOrderDto newOrderDto) {
        dishInOrderList = newOrderDto.getDishInOrderList().stream()
                .map(dishInOrder -> new DishInOrder(dishInOrder.getId(), dishInOrder.getCount(), dishInOrder.getDish(), dishInOrder.getOrder()))
                .collect(Collectors.toList());
        comment = new String(newOrderDto.getComment());
    }
}