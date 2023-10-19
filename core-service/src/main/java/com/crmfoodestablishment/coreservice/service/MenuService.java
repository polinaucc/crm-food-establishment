package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class);

    private Menu checkIfMenuExist(UUID uuid) {
        return menuRepository.findById(uuid)
                .orElseThrow(() -> new MenuNotFoundException("No menu found for this uuid"));

    }

    public UUID addMenu(MenuDto menuDto) {
        boolean uuidExist = menuRepository.existsByUuid(menuDto.getUuid());
        if (uuidExist) {
            throw new IllegalStateException("Menu name: " + menuDto.getName() + " has been taken");
        }

        Menu menuToSave = menuMapper.fromMenuDtoToMenuEntity(menuDto);
        Menu savedMenu = menuRepository.save(menuToSave);

        MenuDto savedMenuDto = menuMapper.fromMenuEntityToMenuDto(savedMenu);
        return savedMenuDto.getUuid();
    }

    public List<MenuDto> findAllMenu() {
        return menuRepository.findAll().stream()
                .map(menuMapper::fromMenuEntityToMenuDto)
                .toList();
    }

    public MenuDto findByMenuId(UUID uuid) {
        Menu menu = checkIfMenuExist(uuid);
        return menuMapper.fromMenuEntityToMenuDto(menu);
    }

    @Transactional
    public MenuDto update(UUID uuid, MenuDto menuDto) {
        checkIfMenuExist(uuid);
        Menu menu = menuMapper.fromMenuDtoToMenuEntity(menuDto);
        Menu savedMenu = menuRepository.save(menu);

        return menuMapper.fromMenuEntityToMenuDto(savedMenu);
    }

    public UUID deleteMenu(UUID uuid) {
        Menu menu = checkIfMenuExist(uuid);
        menuRepository.deleteById(uuid);
        return menu.getUuid();
    }
}
