//package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import static org.assertj.core.api.Java6Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

//@ExtendWith(MockitoExtension.class)
//class MenuServiceTest {
//
//    @Mock
//    MenuRepository menuRepository;
//
//    @InjectMocks
//    private MenuService service;
//
//
//    private static Menu createMenu() {
//        Menu menu = new Menu(
//                "summer menu",
//                "new summer menu",
//                Season.SUMMER
//        );
//        return menu;
//    }
//
//
//    @BeforeEach
//    void setUp() {
//        service = new MenuService(menuRepository);
//    }
//
//    @Test
//    void canAddNewMenu() {
//        Menu menu = createMenu();
//
//        service.addMenu(menu);
//        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);
//        verify(menuRepository)
//                .save(menuArgumentCaptor.capture());
//        Menu captoreMenu = menuArgumentCaptor.getValue();
//
//        assertThat(captoreMenu).isEqualTo(menu);
//    }
//
//    @Test
//    void canGetExceptionWhenMenuIdExist() {
//        Menu menu = createMenu();
//        doReturn(true).when(menuRepository).existsById(menu.getId());
//
//        assertThatThrownBy(() -> service.addMenu(menu))
//                .isInstanceOf(IllegalStateException.class)
//                .hasMessageContaining("Menu name: " + menu.getName() +
//                        " has been taken");
//    }
//
//    @Test
//    void canGetAllMenu() {
//        service.findAllMenu();
//
//        verify(menuRepository).findAll();
//    }
//
//    @Test
//    void canGetMenuWithId() {
//        int id = 1;
//        Menu menu = createMenu();
//
//        when(menuRepository.findById(id))
//                .thenReturn(Optional.of(menu));
//
//        assertThat(menuRepository.findById(id)).isEqualTo(Optional.of(menu));
//    }
//
//    @Test
//    void canGetExceptionWhenIdMenuNotFound() {
//        int id = 1;
//
//        given(menuRepository.findById(id)).willReturn(Optional.empty());
//
//        assertThatThrownBy(() -> service.findByMenuId(id))
//                .isInstanceOf(MenuNotFoundException.class)
//                .hasMessage("No menu found for this id");
//    }
//
//    @Test
//    void canDeleteMenu() {
//        Menu menu = createMenu();
//        service.addMenu(menu);
//        when(menuRepository.findById(menu.getId()))
//                .thenReturn(Optional.of(menu));
//
//        Boolean deleteMenu = service.deleteMenu(menu.getId());
//
//        assertThat(deleteMenu).isTrue();
//    }
//
//    @Test
//    void canUpdateMenu() {
//        Menu menu = createMenu();
//        service.addMenu(menu);
//        when(menuRepository.findById(menu.getId()))
//                .thenReturn(Optional.of(menu));
//        menu.setName("winter menu");
//        menu.setComment("new winter menu");
//        menu.setSeason(Season.WINTER);
//
//        service.update(menu.getId(), menu);
//
//        Menu updatedMenu = service.findByMenuId(menu.getId());
//        assertThat(updatedMenu).isEqualTo(menu);
//    }
//}