package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class);
    private final static String MENU_NOT_FOUND_ERROR_MESSAGE = "Menu with uuid %s is not found";

    @Override
    public UUID addMenu(MenuDto menuDto) {
        if (menuRepository.existsByName(menuDto.getName())) {
            throw new IllegalArgumentException("Menu " + menuDto.getName() + " already exists");
        }
        Menu menuToSave = menuMapper.mapMenuDtoToMenu(menuDto);
        Menu savedMenu = menuRepository.save(menuToSave);

        MenuDto savedMenuDto = menuMapper.mapMenuToMenuDto(savedMenu);
        return savedMenuDto.getUuid();
    }

    @Override
    public List<MenuDto> findAllMenu() {
        return menuRepository.findAll().stream()
                .map(menuMapper::mapMenuToMenuDto)
                .toList();
    }

    @Override
    public MenuDto findByMenuUuid(UUID uuid) {
        Menu foundMenu = menuRepository.getMenuByUuid(uuid)
                .orElseThrow(() -> new MenuNotFoundException(String
                        .format(MENU_NOT_FOUND_ERROR_MESSAGE, uuid)));

        return menuMapper.mapMenuToMenuDto(foundMenu);
    }

    @Override
    public MenuDto updateMenu(UUID uuid, MenuDto menuDto) {
        Menu existingMenu = menuRepository.getMenuByUuid(uuid)
                .orElseThrow(() -> new MenuNotFoundException(String
                        .format(MENU_NOT_FOUND_ERROR_MESSAGE, uuid)));

        existingMenu.setName(menuDto.getName());
        existingMenu.setComment(menuDto.getComment());
        existingMenu.setSeason(menuDto.getSeason());

        Menu savedMenu = menuRepository.save(existingMenu);
        return menuMapper.mapMenuToMenuDto(savedMenu);
    }

    @Override
    public UUID deleteMenu(UUID uuid) {
        menuRepository
                .delete(menuRepository.getMenuByUuid(uuid)
                        .orElseThrow(() -> new MenuNotFoundException(String
                                .format(MENU_NOT_FOUND_ERROR_MESSAGE, uuid))));

        return uuid;
    }
}
