package com.crm.food.establishment.core.mapper;

import com.crm.food.establishment.core.dto.MenuDto;
import com.crm.food.establishment.core.entity.Menu;
import org.mapstruct.Mapping;
import org.mapstruct.Mapper;

@Mapper
public interface MenuMapper {
    MenuDto mapMenuToMenuDto(Menu menu);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    Menu mapMenuDtoToMenu(MenuDto menuDto);
}
