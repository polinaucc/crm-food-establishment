package com.crm.food.establishment.core.controller;

import com.crm.food.establishment.core.service.MenuService;
import com.crm.food.establishment.core.dto.MenuDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/menu")
@RequiredArgsConstructor
public class MenuController {

    private final MenuService menuService;

    @PostMapping
    public ResponseEntity<UUID> createMenu(@Valid @RequestBody MenuDto menuDto) {
        UUID uuid = menuService.addMenu(menuDto);
        return new ResponseEntity<>(uuid, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<MenuDto>> getAllMenuList() {
        List<MenuDto> allMenuDtos = menuService.findAllMenu();
        return new ResponseEntity<>(allMenuDtos, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MenuDto> getMenu(@PathVariable(name = "id") String uuid) {
        MenuDto menuDtoByUuid = menuService.findByMenuUuid(UUID.fromString(uuid));
        return new ResponseEntity<>(menuDtoByUuid, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MenuDto> updateMenu(@PathVariable(name = "id") String uuid,
                                              @Valid @RequestBody MenuDto menuDto) {
        MenuDto updatedMenuDto = menuService.updateMenu(UUID.fromString(uuid), menuDto);
        return new ResponseEntity<>(updatedMenuDto, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<UUID> deleteMenu(@PathVariable(name = "id") String uuid) {
        UUID uuidByDeletedMenu = menuService.deleteMenu(UUID.fromString(uuid));
        return new ResponseEntity<>(uuidByDeletedMenu, HttpStatus.OK);
    }
}
