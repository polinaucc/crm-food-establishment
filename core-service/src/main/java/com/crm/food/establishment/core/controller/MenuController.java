package com.crm.food.establishment.core.controller;

import com.crm.food.establishment.core.dto.MenuDto;
import com.crm.food.establishment.core.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.hibernate.validator.constraints.UUID;
import java.util.List;

@RestController
@RequestMapping("api/menu")
@RequiredArgsConstructor
@Validated
public class MenuController {

    private final MenuService menuService;
    private static final String ID_URL = "/{id}";

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public java.util.UUID createMenu(@Valid @RequestBody MenuDto menuDto) {
        return menuService.addMenu(menuDto);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<MenuDto> getAllMenuList() {
        return menuService.findAllMenu();
    }

    @GetMapping(ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public MenuDto getMenu(@PathVariable @UUID String id) {
        return menuService.findByMenuUuid(id);
    }

    @PutMapping(ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public MenuDto updateMenu(@PathVariable @UUID String id,
                              @Valid @RequestBody MenuDto menuDto) {
        return menuService.updateMenu(id, menuDto);
    }

    @DeleteMapping(ID_URL)
    @ResponseStatus(HttpStatus.OK)
    public java.util.UUID deleteMenu(@PathVariable @UUID String id) {
        return menuService.deleteMenu(id);
    }
}
