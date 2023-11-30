package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface OrderMapper {

    @Mapping(target = "dishes", ignore = true)
    Order mapOrderDtoToOrder(CreateNewOrderDto createNewOrderDto);
}
