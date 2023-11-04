package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.order.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "userUuid", source = "userUuid")
    @Mapping(target = "deliveryMethod", source = "deliveryMethod")
    @Mapping(target = "deliveryDetails", source = "deliveryDetails")
    @Mapping(target = "dishes", ignore = true)
    Order newOrderDtoToOrder(NewOrderDto newOrderDto);
}
