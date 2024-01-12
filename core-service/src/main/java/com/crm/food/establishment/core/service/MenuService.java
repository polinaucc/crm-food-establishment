package com.crm.food.establishment.core.service;

import com.crm.food.establishment.core.dto.MenuDto;
import java.util.List;
import java.util.UUID;

public interface MenuService {

    UUID addMenu(MenuDto menuDto);
    List<MenuDto> findAllMenu();
    MenuDto findByMenuUuid(String uuid);
    MenuDto updateMenu(String uuid, MenuDto menuDto);
    UUID deleteMenu(String uuid);
}
