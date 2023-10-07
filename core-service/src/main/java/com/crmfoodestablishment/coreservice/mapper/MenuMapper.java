package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper (
        componentModel = MappingConstants.ComponentModel.SPRING,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);
    MenuDto fromMenuEntityToMenuDto(Menu menu);
    Menu fromMenuDtoToMenuEntity(MenuDto menuDto);
}