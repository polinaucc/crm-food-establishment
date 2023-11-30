package com.crmfoodestablishment.coreservice.controller;

import com.crmfoodestablishment.coreservice.dto.CreateNewOrderDto;
import com.crmfoodestablishment.coreservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.UUID;

@RestController
@RequestMapping("order-api")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UUID> createOrder(@RequestBody @Valid CreateNewOrderDto createNewOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(createNewOrderDto));
    }
}