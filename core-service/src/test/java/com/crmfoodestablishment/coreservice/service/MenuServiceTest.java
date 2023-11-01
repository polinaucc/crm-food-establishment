package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    private MenuService service;

    @Mock
    MenuRepository menuRepository;

    @Mock
    MenuMapper menuMapper;

    @BeforeEach
    void setUp() {
        service = new MenuService(menuRepository);
    }

    private static Menu createMenu() {
        return Menu.builder()
                .uuid(UUID.randomUUID())
                .name("summer menu")
                .comment("new summer menu")
                .season(Season.SUMMER)
                .build();
    }

    private static MenuDto createMenuDto() {
        return MenuDto.builder()
                .name("summer menu")
                .comment("new summer menu")
                .season(Season.SUMMER)
                .build();
    }

    @Test
    void shouldAddNewMenu() {
        Menu menu = createMenu();
        MenuDto menuDto = createMenuDto();

        when(menuRepository.existsByName(any())).thenReturn(false);
        when(menuRepository.save(any())).thenReturn(menu);
        UUID uuid = service.addMenu(menuDto);

        verify(menuRepository, times(1)).save(any());
        assertNotNull(uuid);
    }

    @Test
    void shouldGetExceptionWhenMenuIdExist() {
        MenuDto menuDto = createMenuDto();

        when(menuRepository.existsByName(any())).thenReturn(true);

        assertThatThrownBy(() -> service.addMenu(menuDto))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Menu " + menuDto.getName() + " is available");
    }

    @Test
    void shouldGetMenuWithId() {
        Menu menu = createMenu();

        when(menuRepository.getMenuByUuid(menu.getUuid())).thenReturn(Optional.of(menu));
        MenuDto menuDto = service.findByMenuUuid(menu.getUuid());

        assertNotNull(menuDto);
        assertEquals(menu.getUuid(), menuDto.getUuid());
    }

    @Test
    void shouldThrowExceptionWhenIdNotFound() {
        UUID uuid = UUID.randomUUID();

        when(menuRepository.getMenuByUuid(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.findByMenuUuid(uuid))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with uuid " + uuid + " is not found");
    }

    @Test
    void shouldGetAllMenu() {
        Menu menu = createMenu();
        Menu menu2 = Menu.builder()
                .uuid(UUID.randomUUID())
                .name("winter dishes")
                .comment("all dishes to winter season")
                .season(Season.WINTER)
                .build();

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu, menu2));
        List<MenuDto> allMenu = service.findAllMenu();

        assertEquals(2, allMenu.size());
    }

    @Test
    void shouldUpdateMenu() {
        Menu menu = createMenu();
        MenuDto menuDto = MenuDto.builder()
                .uuid(menu.getUuid())
                .name("winter menu")
                .comment("new winter menu")
                .season(Season.WINTER)
                .build();

        when(menuRepository.getMenuByUuid(menu.getUuid())).thenReturn(Optional.of(menu));
        MenuDto updatedMenuDto = service.update(menu.getUuid(), menuDto);

        assertThat(updatedMenuDto.getUuid()).isEqualTo(menu.getUuid());
        assertThat(updatedMenuDto.getName()).isEqualTo(menuDto.getName());
        assertThat(updatedMenuDto.getComment()).isEqualTo(menuDto.getComment());
        assertThat(updatedMenuDto.getSeason()).isEqualTo(menuDto.getSeason());
    }

    @Test
    void shouldDeleteMenu() {
        Menu menu = createMenu();

        when(menuRepository.getMenuByUuid(menu.getUuid())).thenReturn(Optional.of(menu));
        UUID deleteMenuUuid = service.deleteMenu(menu.getUuid());

        verify(menuRepository, times(1)).delete(menu);
        assertThat(deleteMenuUuid).isEqualTo(menu.getUuid());
    }
}
