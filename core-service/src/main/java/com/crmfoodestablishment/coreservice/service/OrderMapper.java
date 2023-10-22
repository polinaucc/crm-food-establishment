package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "dishes", ignore = true)
    Order newOrderDtoToOrder(NewOrderDto newOrderDto);
}
