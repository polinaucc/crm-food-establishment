package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.service.MenuService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping("/create")
    public void createMenu(@Validated @RequestBody Menu menu) {
        menuService.addMenu(menu);
    }

    @GetMapping()
    public List<Menu> getAllMenuList() {
        return menuService.findAllMenu();
    }

    @GetMapping("/{id}")
    public String menuView(@PathVariable(name = "id") Integer id) {
        Menu menu = menuService.findByIdMenu(id);
        return menu.toString();
    }

    @PutMapping("/{id}")
    public void updateMenu(@PathVariable(name = "id") Integer id, @RequestBody Menu menu) {
        menuService.update(id, menu);
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable(name = "id") Integer id) {
        menuService.deleteMenu(id);
    }
}
