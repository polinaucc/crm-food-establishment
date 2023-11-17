package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.DishDto;
import com.crmfoodestablishment.coreservice.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/v1/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {

    private final DishService dishService;

    @PostMapping("/{menu_id}")
    public List<DishDto> createDishes(@PathVariable(name = "menu_id") Integer id, @RequestBody List<@Valid DishDto> dishesDto) {
        return dishService.addDishes(id, dishesDto);
    }
}
