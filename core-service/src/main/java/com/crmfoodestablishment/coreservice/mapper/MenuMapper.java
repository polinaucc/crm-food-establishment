package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.*;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MenuMapper {
    MenuDto fromMenuEntityToMenuDto(Menu menu);
    Menu fromMenuDtoToMenuEntity(MenuDto menuDto);
}
