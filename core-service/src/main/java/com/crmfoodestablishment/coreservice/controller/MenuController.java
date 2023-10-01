package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final ModelMapper modelMapper;
    private final MenuService menuService;

    @PostMapping()
    public Integer createMenu(@RequestBody MenuDto menuDto) {
        Menu menuRequest = modelMapper.map(menuDto, Menu.class);
        Menu saveMenu = menuService.addMenu(menuRequest);

        MenuDto menuResponse = modelMapper.map(saveMenu, MenuDto.class);
        return menuResponse.getId();
    }

    @GetMapping()
    public List<MenuDto> getAllMenuList() {
        return menuService.findAllMenu()
                .stream()
                .map(menu -> modelMapper.map(menu, MenuDto.class))
                .toList();
    }

    @GetMapping("/{id}")
    public MenuDto getMenu(@PathVariable(name = "id") Integer id) {
        Menu menu = menuService.findByIdMenu(id);

        return modelMapper.map(menu, MenuDto.class);
    }

    @PutMapping("/{id}")
    public MenuDto updateMenu(@PathVariable(name = "id") Integer id,@Valid @RequestBody MenuDto menuDto) {
        Menu menuRequest = modelMapper.map(menuDto, Menu.class);
        Menu updateMenu = menuService.update(id, menuRequest);

        return modelMapper.map(updateMenu, MenuDto.class);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenu(@PathVariable(name = "id") Integer id) {
        menuService.deleteMenu(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}