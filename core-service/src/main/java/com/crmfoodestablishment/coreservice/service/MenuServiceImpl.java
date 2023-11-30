package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class);

    private MenuNotFoundException throwMenuNotFoundException(UUID uuid) {
        return new MenuNotFoundException("Menu with uuid " + uuid + " is not found");
    }

    @Override
    public UUID addMenu(MenuDto menuDto) {
        if (menuRepository.existsByName(menuDto.getName())) {
            throw new IllegalArgumentException("Menu " + menuDto.getName() + " is available");
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
                .orElseThrow(() -> throwMenuNotFoundException(uuid));

        return menuMapper.mapMenuToMenuDto(foundMenu);
    }

    @Override
    @Transactional
    public MenuDto update(UUID uuid, MenuDto menuDto) {
        return menuRepository.getMenuByUuid(uuid)
                .map(existingMenu -> {
                    existingMenu.setName(menuDto.getName());
                    existingMenu.setComment(menuDto.getComment());
                    existingMenu.setSeason(menuDto.getSeason());
                    return menuMapper.mapMenuToMenuDto(existingMenu);
                }).orElseThrow(() -> throwMenuNotFoundException(uuid));
    }

    @Override
    public UUID deleteMenu(UUID uuid) {
        menuRepository
                .delete(menuRepository.getMenuByUuid(uuid)
                        .orElseThrow(() -> throwMenuNotFoundException(uuid)));

        return uuid;
    }
}
