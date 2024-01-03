package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.ArgumentCaptor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;
import java.util.UUID;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@WebMvcTest(MenuController.class)
@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private MenuDto menuDto;

    @BeforeEach
    public void setUpData() {
        menuDto = new MenuDto();
        menuDto.setUuid(UUID.randomUUID());
        menuDto.setName("summer menu");
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.SUMMER);
    }

    @Test
    void createMenu_ShouldSuccessfullyCreateMenu() throws Exception {
        when(menuService.addMenu(any(MenuDto.class))).thenReturn(menuDto.getUuid());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(menuDto.getUuid().toString()));
    }

    @Test
    void createMenu_ShouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        menuDto.setName(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.title").value("Validation exception"))
                .andExpect(jsonPath("$.errors[0]").value("[name: 'Field name cannot be blank']"));
    }

    @Test
    void createMenu_ShouldSaveMenuWithCorrectValues() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        ArgumentCaptor<MenuDto> menuDtoCaptor = ArgumentCaptor.forClass(MenuDto.class);

        verify(menuService).addMenu(menuDtoCaptor.capture());
        MenuDto actualMenu = menuDtoCaptor.getValue();
        assertThat(actualMenu).isEqualTo(menuDto);
    }

    @Test
    void getAllMenuList_ShouldGetAllMenuList() throws Exception {
        MenuDto menuDto2 = new MenuDto();
        menuDto2.setName("winter dishes");
        menuDto2.setComment("all dishes to winter season");
        menuDto2.setSeason(Season.WINTER);

        List<MenuDto> menuDtosList = List.of(menuDto, menuDto2);
        when(menuService.findAllMenu()).thenReturn(menuDtosList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].name").value(menuDto.getName()))
                .andExpect(jsonPath("$[1].name").value(menuDto2.getName()))
                .andExpect(jsonPath("$[0].comment").value(menuDto.getComment()))
                .andExpect(jsonPath("$[1].comment").value(menuDto2.getComment()));
    }

    @Test
    void getMenu_ShouldGetMenuByUuid() throws Exception {
        when(menuService.findByMenuUuid(any(UUID.class))).thenReturn(menuDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu/{id}", menuDto.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(menuDto.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(menuDto.getName()))
                .andExpect(jsonPath("$.comment").value(menuDto.getComment()))
                .andExpect(jsonPath("$.season").value(menuDto.getSeason().name()));
    }

    @Test
    void updateMenu_ShouldUpdateMenuByUuid() throws Exception {
        MenuDto requestMenu = new MenuDto();
        requestMenu.setName("winter menu");
        requestMenu.setComment("new winter menu");
        requestMenu.setSeason(Season.WINTER);

        MenuDto responseMenu = new MenuDto();
        responseMenu.setUuid(menuDto.getUuid());
        responseMenu.setName("winter menu");
        responseMenu.setComment("new winter menu");
        responseMenu.setSeason(Season.WINTER);

        when(menuService.updateMenu(any(UUID.class), any())).thenReturn(responseMenu);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/menu/{id}", menuDto.getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMenu)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(menuDto.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(responseMenu.getName()))
                .andExpect(jsonPath("$.comment").value(responseMenu.getComment()))
                .andExpect(jsonPath("$.season").value(responseMenu.getSeason().name()));
    }

    @Test
    void deleteMenu_ShouldDeleteMenuByUuid() throws Exception {
        when(menuService.deleteMenu(any(UUID.class))).thenReturn(menuDto.getUuid());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/menu/{id}", menuDto.getUuid()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$").value(menuDto.getUuid().toString()));
    }
}
