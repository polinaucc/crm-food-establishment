package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.MenuDto;
import com.crmfoodestablishment.coreservice.entity.Season;
import com.crmfoodestablishment.coreservice.service.MenuService;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.assertj.core.api.Assertions.assertThat;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.util.List;
import java.util.UUID;

@WebMvcTest(MenuController.class)
@ExtendWith(MockitoExtension.class)
class MenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private MenuController menuController;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private MenuDto createMenuDto() {
        MenuDto menuDto = new MenuDto();
        menuDto.setUuid(UUID.randomUUID());
        menuDto.setName("summer menu");
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.SUMMER);
        return menuDto;
    }

    @Test
    void shouldCreateMenu() throws Exception {
        MenuDto menuDto = createMenuDto();

        when(menuService.addMenu(any(MenuDto.class))).thenReturn(menuDto.getUuid());

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldGet400BadRequestWhenRequestContainNullValue() throws Exception {
        MenuDto menuDto = new MenuDto();
        menuDto.setName(null);
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.SUMMER);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void whenValidInputThenMapsToBusinessModel() throws Exception {
        MenuDto menuDto = createMenuDto();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(MockMvcResultMatchers.status().isOk());
        ArgumentCaptor<MenuDto> menuCaptor = ArgumentCaptor.forClass(MenuDto.class);

        verify(menuService, times(1)).addMenu(menuCaptor.capture());
        assertThat(menuCaptor.getValue().getName()).isEqualTo("summer menu");
        assertThat(menuCaptor.getValue().getComment()).isEqualTo("new summer menu");
        assertThat(menuCaptor.getValue().getSeason().name()).isEqualTo(Season.SUMMER.name());
    }

    @Test
    void whenValidInputThenReturnMenuDto() throws Exception {
        MenuDto menuDto = createMenuDto();
        MenuDto expectedMenuDto = new MenuDto();
        expectedMenuDto.setUuid(menuDto.getUuid());

        when(menuService.addMenu(any(MenuDto.class))).thenReturn(menuDto.getUuid());
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/api/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn();
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertThat(actualResponseBody).isEqualToIgnoringWhitespace(
                objectMapper.writeValueAsString(expectedMenuDto.getUuid()));
    }

    @Test
    void shouldGetAllMenuList() throws Exception {
        MenuDto menuDto = createMenuDto();
        MenuDto menuDto2 = new MenuDto();
        menuDto2.setName("winter dishes");
        menuDto2.setComment("all dishes to winter season");
        menuDto2.setSeason(Season.WINTER);

        List<MenuDto> menuDtoList = List.of(menuDto, menuDto2);
        when(menuService.findAllMenu()).thenReturn(menuDtoList);

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
    void shouldGetMenuByUuid() throws Exception {
        MenuDto menuDto = createMenuDto();

        when(menuService.findByMenuUuid(any(UUID.class))).thenReturn(menuDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu/{uuid}", menuDto.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(menuDto.getUuid().toString()))
                .andExpect(jsonPath("$.name").value(menuDto.getName()))
                .andExpect(jsonPath("$.comment").value(menuDto.getComment()))
                .andExpect(jsonPath("$.season").value(menuDto.getSeason().name()));
    }

    @Test
    void shouldUpdateMenuByUuid() throws Exception {
        MenuDto menuDto = createMenuDto();
        MenuDto requestMenu = new MenuDto();
        requestMenu.setName("winter menu");
        requestMenu.setComment("new winter menu");
        requestMenu.setSeason(Season.WINTER);
        MenuDto responseMenu = new MenuDto();
        responseMenu.setName("winter menu");
        requestMenu.setComment("new winter menu");
        requestMenu.setSeason(Season.WINTER);

        when(menuService.update(any(UUID.class), any())).thenReturn(responseMenu);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/menu/{uuid}", menuDto.getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMenu)))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldDeleteMenuByUuid() throws Exception {
        MenuDto menuDto = createMenuDto();

        when(menuService.deleteMenu(any(UUID.class))).thenReturn(menuDto.getUuid());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/menu/{uuid}", menuDto.getUuid())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
