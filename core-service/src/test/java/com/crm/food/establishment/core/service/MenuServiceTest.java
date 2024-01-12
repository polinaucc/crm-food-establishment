package com.crm.food.establishment.core.service;

import com.crm.food.establishment.core.dto.MenuDto;
import com.crm.food.establishment.core.entity.Season;
import com.crm.food.establishment.core.mapper.MenuMapper;
import com.crm.food.establishment.core.repository.MenuRepository;
import com.crm.food.establishment.core.entity.Menu;
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
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuServiceTest {
    @InjectMocks
    private MenuServiceImpl menuServiceImpl;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuMapper menuMapper;

    private Menu menu;
    private UUID menuId;

    private static MenuDto formMenuDto(String name, String comment, Season season) {
        MenuDto menuDto = new MenuDto();
        menuDto.setName(name);
        menuDto.setComment(comment);
        menuDto.setSeason(season);
        return menuDto;
    }

    @BeforeEach
    public void setUpData() {
        menuId = UUID.randomUUID();

        menu = new Menu();
        menu.setUuid(menuId);
        menu.setName("summer menu");
        menu.setComment("new summer menu");
        menu.setSeason(Season.SUMMER);
    }

    @Test
    void addMenu_ShouldAddNewMenu() {
        MenuDto menuDto = formMenuDto("summer menu", "new summer menu", Season.SUMMER);

        when(menuRepository.save(menu)).thenReturn(menu);
        ArgumentCaptor<Menu> menuArgumentCaptor = ArgumentCaptor.forClass(Menu.class);

        menuServiceImpl.addMenu(menuDto);

        verify(menuRepository).save(menuArgumentCaptor.capture());

        Menu actualMenu = menuArgumentCaptor.getValue();
        assertThat(actualMenu).isEqualTo(menu);
    }

    @Test
    void addMenu_ShouldGetExceptionWhenMenuNameExist() {
        MenuDto menuDto = formMenuDto("summer menu", "new summer menu", Season.SUMMER);

        when(menuRepository.existsByName(menuDto.getName())).thenReturn(true);

        assertThatThrownBy(() -> menuServiceImpl.addMenu(menuDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Menu with name [" + menuDto.getName() + "] already exists");
    }

    @Test
    void findByMenuUuid_ShouldGetMenuWithUuid() {
        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.of(menu));

        MenuDto actualMenu = menuServiceImpl.findByMenuUuid(menuId.toString());

        assertThat(actualMenu).usingRecursiveComparison().isEqualTo(menu);
    }

    @Test
    void findByMenuUuid_ShouldThrowExceptionWhenUuidNotFound() {
        when(menuRepository.getMenuByUuid(menuId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> menuServiceImpl.findByMenuUuid(menuId.toString()))
                .isInstanceOf(MenuNotFoundException.class)
                .hasMessage("Menu with uuid [" + menuId + "] is not found");
    }

    @Test
    void findAllMenu_ShouldGetAllMenu() {
        Menu menu2 = new Menu();
        menu2.setUuid(UUID.randomUUID());
        menu2.setName("winter dishes");
        menu2.setComment("all dishes to winter season");
        menu2.setSeason(Season.WINTER);

        List<Menu> expectedMenuList = Arrays.asList(menu, menu2);

        when(menuRepository.findAll()).thenReturn(expectedMenuList);
        List<MenuDto> actualMenuList = menuServiceImpl.findAllMenu();

        assertThat(actualMenuList).usingRecursiveComparison().isEqualTo(expectedMenuList);
    }

    @Test
    void updateMenu_ShouldUpdateMenuByUuid() {
        MenuDto expectedMenuDto = new MenuDto();
        expectedMenuDto.setUuid(menu.getUuid());
        expectedMenuDto.setName("winter menu");
        expectedMenuDto.setComment("new summer menu");
        expectedMenuDto.setSeason(Season.WINTER);

        when(menuRepository.getMenuByUuid(menu.getUuid())).thenReturn(Optional.of(menu));
        when(menuRepository.save(menu)).thenReturn(menu);

        MenuDto actualMenuDto = menuServiceImpl.updateMenu(menu.getUuid().toString(), expectedMenuDto);

        assertThat(actualMenuDto).usingRecursiveComparison().isEqualTo(expectedMenuDto);
    }

    @Test
    void deleteMenu_shouldDeleteMenuByUuid() {
        when(menuRepository.getMenuByUuid(menu.getUuid())).thenReturn(Optional.of(menu));

        UUID actualMenuUuid = menuServiceImpl.deleteMenu(menu.getUuid().toString());

        verify(menuRepository).delete(menu);
        assertThat(actualMenuUuid).isEqualTo(menu.getUuid());
    }
}
