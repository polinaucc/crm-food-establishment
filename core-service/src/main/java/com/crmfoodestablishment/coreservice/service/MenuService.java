package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import java.util.List;
import java.util.UUID;

public interface MenuService {

    UUID addMenu(MenuDto menuDto);

    List<MenuDto> findAllMenu();

    MenuDto findByMenuUuid(UUID uuid);

    MenuDto update(UUID uuid, MenuDto menuDto);

    UUID deleteMenu(UUID uuid);
}
