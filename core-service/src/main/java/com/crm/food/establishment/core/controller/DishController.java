package com.crm.food.establishment.core.controller;

import com.crm.food.establishment.core.dto.CreateDishDto;
import com.crm.food.establishment.core.service.DishService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.hibernate.validator.constraints.UUID;
import java.util.List;

@RestController
@RequestMapping("api/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {

    private final DishService dishService;

    @PostMapping("/{menuId}")
    public List<java.util.UUID> createDishes(@PathVariable @UUID String menuId,
                                                             @RequestBody @NotEmpty List<@Valid CreateDishDto> dishesDto) {
        return dishService.addDishes(menuId, dishesDto);
    }
}
