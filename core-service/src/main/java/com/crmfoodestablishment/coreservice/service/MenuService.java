package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class);

    private Optional<Menu> checkIfMenuUuidExistAndReturnThisMenu(UUID uuid) {

        boolean existsByUuid = menuRepository.existsByUuid(uuid);

        if (existsByUuid) {
            return menuRepository.getMenuByUuid(uuid);
        } else {
            throw new MenuNotFoundException("Menu with uuid " + uuid + " is not found");
        }
    }

    private void nonValidDataOfRequest() {
        throw new InvalidArgumentException("Incorrect data entered");
    }

    public UUID addMenu(MenuDto menuDto) {
        boolean existsByName = menuRepository.existsByName(menuDto.getName());

        if (existsByName) {
            throw new IllegalStateException("Menu " + menuDto.getName() + " is available");
        }

        Menu menuEntity = menuMapper.fromMenuDtoToMenuEntity(menuDto);
        menuEntity.setUuid(UUID.randomUUID());
        Menu savedMenu = menuRepository.save(menuEntity);

        MenuDto savedMenuDto = menuMapper.fromMenuEntityToMenuDto(savedMenu);
        return savedMenuDto.getUuid();
    }

    public List<MenuDto> findAllMenu() {
        return menuRepository.findAll().stream()
                .map(menuMapper::fromMenuEntityToMenuDto)
                .toList();
    }

    public MenuDto findByMenuUuid(UUID uuid) {

        Optional<Menu> menu = checkIfMenuUuidExistAndReturnThisMenu(uuid);

        return menuMapper.fromMenuEntityToMenuDto(menu.get());

    }

    @Transactional
    public MenuDto update(UUID uuid, MenuDto menuDto) {

        Optional<Menu> menu = checkIfMenuUuidExistAndReturnThisMenu(uuid);

        menu.ifPresentOrElse(existMenu -> {
            existMenu.setName(menuDto.getName());
            existMenu.setComment(menuDto.getComment());
            existMenu.setSeason(menuDto.getSeason());
        }, this::nonValidDataOfRequest);

        return menuMapper.fromMenuEntityToMenuDto(menu.get());
    }

    public UUID deleteMenu(UUID uuid) {
        Optional<Menu> menuOptional = checkIfMenuUuidExistAndReturnThisMenu(uuid);

        menuRepository.delete(menuOptional.get());

        return uuid;
    }
}
