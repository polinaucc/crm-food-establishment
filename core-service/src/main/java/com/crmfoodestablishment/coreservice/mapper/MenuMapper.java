package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MenuMapper {
    MenuDto mapMenuToMenuDto(Menu menu);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    Menu mapMenuDtoToMenu(MenuDto menuDto);
}
