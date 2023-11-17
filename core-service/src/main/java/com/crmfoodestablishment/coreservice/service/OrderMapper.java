package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.order.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "dishes", ignore = true)
    Order mapOrderDtoToOrder(CreateNewOrderDto createNewOrderDto);
}
