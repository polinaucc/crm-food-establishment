package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class MenuController {

    private final MenuRepository menuRepository;

    @PostMapping("/menu/create")
    public void createMenu(Menu menu) {
        menuRepository.save(menu);
    }

    @GetMapping("/menu")
    public List<Menu> getAllStudent() {
        return menuRepository.findAll();
    }

    @GetMapping("/menu/{id}")
    public Menu menuView(@PathVariable Integer id) {
        Optional<Menu> currentMenu = menuRepository.findById(id);

        if (currentMenu.isEmpty()) {
            throw new MenuNotFoundException("Menu not exist with id: " + id);
        }

        return currentMenu.get();
    }

    @PutMapping("/menu/{id}")
    public ResponseEntity<Menu> updateMenu(@PathVariable Integer id, @RequestBody Menu menu) {
        Optional<Menu> currentMenu = menuRepository.findById(id);

        if (currentMenu.isEmpty()) {
            throw new MenuNotFoundException("Menu not exist with id: " + id);
        }

        menu.setId(id);
        menuRepository.save(menu);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/menu/{id}")
    public void deleteMenu(@PathVariable Integer id) {
        menuRepository.deleteById(id);
    }
}