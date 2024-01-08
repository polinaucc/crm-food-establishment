package com.crm.food.establishment.core.controller;

import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.service.DishService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {

    private final DishService dishService;

    @PostMapping("/{menuId}")
    @ResponseStatus(HttpStatus.CREATED)
    public List<UUID> createDishes(@PathVariable(name = "menuId") String menuId,
                                   @RequestBody List<@Valid CreateDishDto> dishesDto) { //TODO: add validation that list of dishes is not empty
        return dishService.addDishes(menuId, dishesDto);
    }

}
