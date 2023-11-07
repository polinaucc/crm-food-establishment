package com.crmfoodestablishment.coreservice.mapper;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import org.mapstruct.*;
import java.util.UUID;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface MenuMapper {
    MenuDto mapMenuToMenuDto(Menu menu);
    Menu mapMenuDtoToMenu(MenuDto menuDto);

    default void setUuid(Menu menu) {
        menu.setUuid(UUID.randomUUID());
    }
}
