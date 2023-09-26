package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private Menu getMenu(Integer id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException("No menu found for this id"));
    }

    public void addMenu(Menu menu) {
        boolean idExist = menuRepository.existsById(menu.getId());
        if (idExist) {
            throw new IllegalStateException("Menu name " + menu.getName() + " has been taken");
        }
        menuRepository.save(menu);
    }

    public List<Menu> findAllMenu() {
        return menuRepository.findAll();
    }

    public Menu findByIdMenu(Integer id) {
        return getMenu(id);
    }

    @Transactional
    public void update(Integer id, Menu menu) {
        getMenu(id);

        if (menu.getName() != null && !menu.getName().isEmpty()) {
            menu.setName(menu.getName());
        }
        if (menu.getComment() != null) {
            menu.setComment(menu.getComment());
        }
        if (menu.getSeason() != null && !menu.getSeason().toString().isEmpty()) {
            menu.setSeason(Season.valueOf(String.valueOf(menu.getSeason())));
        }
        menuRepository.save(menu);
    }

    public void deleteMenu(Integer id) {
        getMenu(id);
        menuRepository.deleteById(id);
    }
}