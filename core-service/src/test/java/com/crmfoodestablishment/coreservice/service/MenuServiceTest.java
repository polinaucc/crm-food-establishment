package com.crmfoodestablishment.coreservice.service;

import com.crm.food.establishment.core.dto.MenuDto;
import com.crm.food.establishment.core.entity.Menu;
import com.crm.food.establishment.core.entity.Season;
import com.crm.food.establishment.core.mapper.MenuMapper;
import com.crm.food.establishment.core.repository.MenuRepository;
import com.crm.food.establishment.core.service.MenuNotFoundException;
import com.crm.food.establishment.core.service.MenuServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.anyString;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    private MenuServiceImpl menuServiceImpl;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuMapper menuMapper;

    private Menu menu;
    private MenuDto menuDto;

    @BeforeEach
    public void setUpData() {
        menu = new Menu();
        menu.setUuid(UUID.randomUUID());
        menu.setName("summer menu");
        menu.setComment("new summer menu");
        menu.setSeason(Season.SUMMER);

        menuDto = new MenuDto();
        menuDto.setName("summer menu");
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.SUMMER);
    }

    @Test
    void addMenu_ShouldAddNewMenu() {
        when(menuRepository.existsByName(anyString())).thenReturn(false);
        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);
        when(menuRepository.save(any())).thenReturn(menu);
        UUID uuid = menuServiceImpl.addMenu(menuDto);

        verify(menuRepository).save(menuArgumentCaptor.capture());
        assertNotNull(uuid);
        Menu actualMenu = menuArgumentCaptor.getValue();
        assertThat(actualMenu).usingRecursiveComparison().ignoringFields("id", "uuid")
                .isEqualTo(menuDto);
    }

    @Test
    void addMenu_ShouldGetExceptionWhenMenuNameExist() {
        when(menuRepository.existsByName(anyString())).thenReturn(true);

        assertThatThrownBy(() -> menuServiceImpl.addMenu(menuDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu " + menuDto.getName() + " already exists");
    }

    @Test
    void findByMenuUuid_ShouldGetMenuWithUuid() {
        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.of(menu));
        MenuDto responseMenuDto = menuServiceImpl.findByMenuUuid(menu.getUuid());

        assertThat(responseMenuDto).usingRecursiveComparison().isEqualTo(menu);
    }

    @Test
    void findByMenuUuid_ShouldThrowExceptionWhenUuidNotFound() {
        UUID menuId = UUID.randomUUID();

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuServiceImpl.findByMenuUuid(menuId))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with uuid " + menuId + " is not found");
    }

    @Test
    void findAllMenu_ShouldGetAllMenu() {
        Menu menu2 = new Menu();
        menu2.setUuid(UUID.randomUUID());
        menu2.setName("winter dishes");
        menu2.setComment("all dishes to winter season");
        menu2.setSeason(Season.WINTER);

        when(menuRepository.findAll()).thenReturn(Arrays.asList(menu, menu2));
        List<MenuDto> responseMenuDtos = menuServiceImpl.findAllMenu();

        Assertions.assertThat(responseMenuDtos).hasSize(2);
        //TODO: this comparising migtht be changed - compare lists
        assertThat(responseMenuDtos.get(0)).usingRecursiveComparison()
                .isEqualTo(menu);
        assertThat(responseMenuDtos.get(1)).usingRecursiveComparison()
                .isEqualTo(menu2);
    }

    @Test
    void updateMenu_ShouldUpdateMenuByUuid() {
        MenuDto requestMenuDto = new MenuDto();
        requestMenuDto.setUuid(menu.getUuid());
        requestMenuDto.setName("winter menu");
        requestMenuDto.setComment("new summer menu");
        requestMenuDto.setSeason(Season.WINTER);

        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.of(menu));
        when(menuRepository.save(any())).thenReturn(menu);
        MenuDto responseMenuDto = menuServiceImpl.updateMenu(menu.getUuid(), requestMenuDto);

        assertThat(responseMenuDto).usingRecursiveComparison()
                .isEqualTo(requestMenuDto);
    }

    @Test
    void deleteMenu_shouldDeleteMenuByUuid() {
        when(menuRepository.getMenuByUuid(any(UUID.class))).thenReturn(Optional.of(menu));
        UUID deleteMenuUuid = menuServiceImpl.deleteMenu(menu.getUuid());

        verify(menuRepository).delete(menu);
        assertThat(deleteMenuUuid).isEqualTo(menu.getUuid());
    }
}
