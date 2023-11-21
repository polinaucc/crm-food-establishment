package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("api/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {

    private final DishService dishService;

    @PostMapping("/{menuId}")
    public List<CreateDishDto> createDishes(@PathVariable(name = "menuId") Integer id, @RequestBody List<@Valid CreateDishDto> dishesDto) {
        return dishService.addDishes(id, dishesDto);
    }
}
