package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.exception.MenuNotFoundException;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {

    @Mock
    MenuRepository menuRepository;

    @InjectMocks
    private MenuService underTest;

    @BeforeEach
    void setUp() {
        underTest = new MenuService(menuRepository);
    }

    @Test
    void canAddNewMenu() {
        Menu menu = new Menu(
                "summer menu",
                "new summer menu",
                Season.SUMMER
        );

        underTest.addMenu(menu);

        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);

        verify(menuRepository)
                .save(menuArgumentCaptor.capture());

        Menu captoreMenu = menuArgumentCaptor.getValue();
        assertThat(captoreMenu).isEqualTo(menu);
    }

    @Test
    void canGetExceptionWhenMenuIdExist() {
        Menu menu = new Menu(
                "summer menu",
                "new summer menu",
                Season.SUMMER
        );
        doReturn(true).when(menuRepository).existsById(menu.getId());

        assertThatThrownBy(() -> underTest.addMenu(menu))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("Menu name " + menu.getName() +
                        " has been taken");
    }

    @Test
    void canGetAllMenu() {
        underTest.findAllMenu();

        verify(menuRepository).findAll();
    }

    @Test
    void canGetMenuWithId() {
        int id = 1;
        Menu menu = new Menu(
                "summer menu",
                "new summer menu",
                Season.SUMMER
        );

        when(menuRepository.findById(id))
                .thenReturn(Optional.of(menu));

        assertThat(menuRepository.findById(id)).isEqualTo(Optional.of(menu));
    }

    @Test
    void canGetExceptionWhenIdMenuNotFound() {
        int id = 1;
        Menu menu = new Menu(
                "summer menu",
                "new summer menu",
                Season.SUMMER
        );

        given(menuRepository.findById(id)).willReturn(Optional.empty());

        assertThatThrownBy(() -> underTest.findByIdMenu(id))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("No menu found for this id");
    }

    @Test
    @Disabled
    void canDeleteMenu() {
        int id = 1;
        Menu menu = new Menu(id,
                "summer menu",
                "new summer menu",
                Season.SUMMER
        );

        underTest.deleteMenu(id);

        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);
        verify(menuRepository).delete(menuArgumentCaptor.capture());
//        Menu captoreMenu = menuArgumentCaptor.getValue();
//        assertThat(captoreMenu).isEqualTo(menu);
    }

    @Test
    @Disabled
    void update() {

    }
}