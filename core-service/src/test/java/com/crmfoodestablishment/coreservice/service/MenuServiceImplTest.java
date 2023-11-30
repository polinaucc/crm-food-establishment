package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Menu;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.mapper.MenuMapper;
import com.crmfoodestablishment.coreservice.repository.MenuRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {
    @InjectMocks
    private MenuServiceImpl menuServiceImpl;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuMapper menuMapper;

    private Menu createMenu() {
        Menu menu = new Menu();
        menu.setUuid(UUID.randomUUID());
        menu.setName("summer menu");
        menu.setComment("new summer menu");
        menu.setSeason(Season.SUMMER);
        return menu;
    }

    private MenuDto createMenuDto() {
        MenuDto menuDto = new MenuDto();
        menuDto.setName("summer menu");
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.SUMMER);
        return menuDto;
    }

    @Test
    void shouldAddNewMenu() {
        Menu menu = createMenu();
        MenuDto menuDto = createMenuDto();

        when(menuRepository.existsByName(anyString())).thenReturn(false);
        ArgumentCaptor<Menu> argumentCaptor = ArgumentCaptor.forClass(Menu.class);
        when(menuRepository.save(any())).thenReturn(menu);
        UUID uuid = menuServiceImpl.addMenu(menuDto);

        verify(menuRepository, times(1)).save(argumentCaptor.capture());
        assertNotNull(uuid);
        Menu menuCaptorValue = argumentCaptor.getValue();
        assertEquals(menuDto.getName(), menuCaptorValue.getName());
        assertEquals(menuDto.getComment(), menuCaptorValue.getComment());
        assertEquals(menuDto.getSeason(), menuCaptorValue.getSeason());
    }

    @Test
    void shouldGetExceptionWhenMenuNameExist() {
        MenuDto menuDto = createMenuDto();

        when(menuRepository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> menuServiceImpl.addMenu(menuDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu " + menuDto.getName() + " is available");
    }

    @Test
    void shouldMenuFieldsMatchMenuDtoFields() {
        MenuDto menuDto = createMenuDto();
        Menu menu = createMenu();

        when(menuRepository.existsByName(anyString())).thenReturn(false);
        when(menuRepository.save(any())).thenReturn(menu);
        menuServiceImpl.addMenu(menuDto);

        assertThat(menu.getName()).isEqualTo(menuDto.getName());
        assertThat(menu.getComment()).isEqualTo(menuDto.getComment());
        assertThat(menu.getSeason().name()).isEqualTo(menuDto.getSeason().name());
    }

    @Test
    void shouldGetMenuWithUuid() {
        Menu menu = createMenu();

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.of(menu));
        MenuDto menuDto = menuServiceImpl.findByMenuUuid(menu.getUuid());

        assertNotNull(menuDto);
        assertEquals(menu.getUuid(), menuDto.getUuid());
    }

    @Test
    void shouldThrowExceptionWhenUuidNotFound() {
        UUID uuid = UUID.randomUUID();

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuServiceImpl.findByMenuUuid(uuid))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with uuid " + uuid + " is not found");
    }

    @Test
    void shouldGetAllMenu() {
        Menu menu = createMenu();
        Menu menu2 = new Menu();
        menu2.setUuid(UUID.randomUUID());
        menu2.setName("winter dishes");
        menu2.setComment("all dishes to winter season");
        menu2.setSeason(Season.WINTER);

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu, menu2));
        List<MenuDto> allMenuDtos = menuServiceImpl.findAllMenu();

        assertEquals(2, allMenuDtos.size());
        assertEquals(menu.getName(), allMenuDtos.get(0).getName());
        assertEquals(menu.getComment(), allMenuDtos.get(0).getComment());
        assertEquals(menu.getSeason(), allMenuDtos.get(0).getSeason());
        assertEquals(menu2.getName(), allMenuDtos.get(1).getName());
        assertEquals(menu2.getComment(), allMenuDtos.get(1).getComment());
        assertEquals(menu2.getSeason(), allMenuDtos.get(1).getSeason());
    }

    @Test
    void shouldUpdateMenuByUuid() {
        Menu menu = createMenu();
        MenuDto menuDto = new MenuDto();
        menuDto.setUuid(menu.getUuid());
        menuDto.setName("winter menu");
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.WINTER);

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.of(menu));
        MenuDto updatedMenuDto = menuServiceImpl.update(menu.getUuid(), menuDto);

        assertThat(updatedMenuDto.getUuid()).isEqualTo(menu.getUuid());
        assertThat(updatedMenuDto.getName()).isEqualTo(menuDto.getName());
        assertThat(updatedMenuDto.getComment()).isEqualTo(menuDto.getComment());
        assertThat(updatedMenuDto.getSeason()).isEqualTo(menuDto.getSeason());
    }

    @Test
    void shouldDeleteMenuByUuid() {
        Menu menu = createMenu();

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.of(menu));
        UUID deleteMenuUuid = menuServiceImpl.deleteMenu(menu.getUuid());

        verify(menuRepository, times(1)).delete(menu);
        assertThat(deleteMenuUuid).isEqualTo(menu.getUuid());
    }
}
