package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper
public interface MenuMapper {
    MenuDto mapMenuToMenuDto(Menu menu);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    Menu mapMenuDtoToMenu(MenuDto menuDto);
}
