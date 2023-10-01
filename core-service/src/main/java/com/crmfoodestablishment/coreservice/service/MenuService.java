package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuRepository menuRepository;

    private Menu checkIfMenuExist(Integer id) {
        return menuRepository.findById(id)
                .orElseThrow(() -> new MenuNotFoundException("No menu found for this id"));
    }

    public Menu addMenu(Menu menu) {
        boolean idExist = menuRepository.existsById(menu.getId());
        if (idExist) {
            throw new IllegalStateException("Menu name: " + menu.getName() + " has been taken");
        }
        return menuRepository.save(menu);
    }

    public List<Menu> findAllMenu() {
        return menuRepository.findAll();
    }

    public Menu findByIdMenu(Integer id) {
        return checkIfMenuExist(id);
    }

    @Transactional
    public Menu update(Integer id, Menu menu) {
        checkIfMenuExist(id);
        return menuRepository.save(menu);
    }

    public Boolean deleteMenu(Integer id) {
        checkIfMenuExist(id);
        menuRepository.deleteById(id);
        return true;
    }
}