package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping()
    public Integer createMenu(@RequestBody MenuDto menuDto) {
        return menuService.addMenu(menuDto);
    }

    @GetMapping()
    public List<MenuDto> getAllMenuList() {
        return menuService.findAllMenu();
    }

    @GetMapping("/{id}")
    public MenuDto getMenu(@PathVariable(name = "id") Integer id) {
        return menuService.findByMenuId(id);
    }

    @PutMapping("/{id}")
    public MenuDto updateMenu(@PathVariable(name = "id") Integer id,@Valid @RequestBody MenuDto menuDto) {
        return menuService.update(id, menuDto);
    }

    @DeleteMapping("/{id}")
    public Integer deleteMenu(@PathVariable(name = "id") Integer id) {
        return menuService.deleteMenu(id);
    }
}