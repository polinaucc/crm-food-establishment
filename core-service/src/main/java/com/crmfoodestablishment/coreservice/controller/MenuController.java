package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public UUID createMenu(@Valid @RequestBody MenuDto menuDto) {
        return menuService.addMenu(menuDto);
    }

    @GetMapping
    public List<MenuDto> getAllMenuList() {
        return menuService.findAllMenu();
    }

    @GetMapping("/{id}")
    public MenuDto getMenu(@PathVariable(name = "id") UUID uuid) {
        return menuService.findByMenuUuid(uuid);
    }

    @PutMapping("/{id}")
    public MenuDto updateMenu(@PathVariable(name = "id") UUID uuid, @Valid @RequestBody MenuDto menuDto) {
        return menuService.update(uuid, menuDto);
    }

    @DeleteMapping("/{id}")
    public UUID deleteMenu(@PathVariable(name = "id") UUID uuid) {
        return menuService.deleteMenu(uuid);
    }
}
