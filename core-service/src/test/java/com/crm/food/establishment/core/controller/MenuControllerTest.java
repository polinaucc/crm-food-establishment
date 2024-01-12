package com.crm.food.establishment.core.controller;

import com.crm.food.establishment.core.dto.MenuDto;
import com.crm.food.establishment.core.entity.Season;
import com.crm.food.establishment.core.service.MenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.util.List;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuController.class)
@ExtendWith(MockitoExtension.class)
class MenuControllerTest {
    private static final String MENU_URL = "/api/menu";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Autowired
    private ObjectMapper objectMapper;

    private MenuDto menuDto;

    private String menuId;

    @BeforeEach
    public void setUpData() {
        menuId = UUID.randomUUID().toString();

        menuDto = new MenuDto();
        menuDto.setUuid(UUID.fromString(menuId));
        menuDto.setName("summer menu");
        menuDto.setComment("new summer menu");
        menuDto.setSeason(Season.SUMMER);
    }

    @Test
    void createMenu_ShouldSuccessfullyCreateMenu() throws Exception {
        when(menuService.addMenu(menuDto)).thenReturn(menuDto.getUuid());

        MockHttpServletRequestBuilder requestBuilder = buildPostRequest(MENU_URL, menuDto);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").value(menuDto.getUuid().toString()));
    }

    @Test
    void createMenu_ShouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        menuDto.setName(null);

        MockHttpServletRequestBuilder requestBuilder = buildPostRequest(MENU_URL, menuDto);
        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[name: 'Field name cannot be blank']"));
    }

    @Test
    void getAllMenuList_ShouldGetAllMenuList() throws Exception {
        MenuDto menuDto2 = new MenuDto();
        menuDto2.setName("winter dishes");
        menuDto2.setComment("all dishes to winter season");
        menuDto2.setSeason(Season.WINTER);

        List<MenuDto> expectedResponse = List.of(menuDto, menuDto2);

        when(menuService.findAllMenu()).thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(MENU_URL))
                .andExpect(status().isOk())
                .andReturn();

        List<MenuDto> actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), new TypeReference<>() {
        });

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void getMenu_ShouldGetMenuByUuid() throws Exception {
        when(menuService.findByMenuUuid(menuId)).thenReturn(menuDto);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.get(MENU_URL + "/{id}", menuDto.getUuid()))
                .andExpect(status().isOk())
                .andReturn();

        MenuDto actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), MenuDto.class);

        assertThat(actualResponse).isEqualTo(menuDto);
    }

    @Test
    void getMenu_ShouldThrow400BadRequestWhenMenuUuidSomeText() throws Exception {
        String text = "text";

        when(menuService.findByMenuUuid(text)).thenReturn(null);

        mockMvc.perform(MockMvcRequestBuilders.get(MENU_URL + "/{id}", text))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].title").value("Validation exception"))
                .andExpect(jsonPath("$[0].error").value("[getMenu.id: 'must be a valid UUID']"));
    }

    @Test
    void updateMenu_ShouldUpdateMenuByUuid() throws Exception {
        MenuDto requestMenu = new MenuDto();
        requestMenu.setName("winter menu");
        requestMenu.setComment("new winter menu");
        requestMenu.setSeason(Season.WINTER);

        MenuDto expectedResponse = new MenuDto();
        expectedResponse.setUuid(menuDto.getUuid());
        expectedResponse.setName("winter menu");
        expectedResponse.setComment("new winter menu");
        expectedResponse.setSeason(Season.WINTER);

        when(menuService.updateMenu(menuId, requestMenu)).thenReturn(expectedResponse);

        MvcResult response = mockMvc.perform(MockMvcRequestBuilders.put(MENU_URL + "/{id}", menuDto.getUuid())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestMenu)))
                .andExpect(status().isOk())
                .andReturn();

        MenuDto actualResponse = objectMapper.readValue(response.getResponse().getContentAsString(), MenuDto.class);

        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    void deleteMenu_ShouldDeleteMenuByUuid() throws Exception {
        when(menuService.deleteMenu(menuId)).thenReturn(menuDto.getUuid());

        mockMvc.perform(MockMvcRequestBuilders.delete(MENU_URL + "/{id}", menuDto.getUuid()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(menuDto.getUuid().toString()));
    }

    private MockHttpServletRequestBuilder buildPostRequest(String url, Object requestBody) throws JsonProcessingException {
        return MockMvcRequestBuilders.post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestBody));
    }
}
