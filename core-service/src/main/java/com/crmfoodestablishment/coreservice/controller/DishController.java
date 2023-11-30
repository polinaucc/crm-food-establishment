package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateDishDto;
import com.crmfoodestablishment.coreservice.service.DishServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@RestController
@RequestMapping("api/dishes")
@RequiredArgsConstructor
@Validated
public class DishController {

    private final DishServiceImpl dishServiceImpl;

    @PostMapping("/{menuId}")
    public ResponseEntity<List<CreateDishDto>> createDishes(@PathVariable(name = "menuId") Integer id, @RequestBody List<@Valid CreateDishDto> dishesDto) {
        List<CreateDishDto> createDishDtos = dishServiceImpl.addDishes(id, dishesDto);
        return new ResponseEntity<>(createDishDtos, HttpStatus.CREATED);
    }
}
