package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private final MenuMapper menuMapper;

    private Menu checkIfMenuExist(Integer id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException("No menu found for this id"));

    }

    public Integer addMenu(MenuDto menuDto) {
        boolean idExist = menuRepository.existsById(menuDto.getId());
        if (idExist) {
            throw new IllegalStateException("Menu name: " + menuDto.getName() + " has been taken");
        }
        Menu menuToSave = menuMapper.fromMenuDtoToMenuEntity(menuDto);
        Menu savedMenu = menuRepository.save(menuToSave);

        MenuDto savedMenuDto = menuMapper.fromMenuEntityToMenuDto(savedMenu);
        return savedMenuDto.getId();
    }

    public List<MenuDto> findAllMenu() {
        return menuRepository.findAll().stream()
                .map(menuMapper::fromMenuEntityToMenuDto)
                .toList();
    }

    public MenuDto findByMenuId(Integer id) {
        Menu menu = checkIfMenuExist(id);
        return menuMapper.fromMenuEntityToMenuDto(menu);
    }

    @Transactional
    public MenuDto update(Integer id, MenuDto menuDto) {
        checkIfMenuExist(id);
        Menu menu = menuMapper.fromMenuDtoToMenuEntity(menuDto);
        Menu savedMenu = menuRepository.save(menu);

        return menuMapper.fromMenuEntityToMenuDto(savedMenu);
    }

    public Integer deleteMenu(Integer id) {
        checkIfMenuExist(id);
        menuRepository.deleteById(id);
        return id;
    }
}