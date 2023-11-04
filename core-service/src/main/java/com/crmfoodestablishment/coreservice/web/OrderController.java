package com.crmfoodestablishment.coreservice.web;

import com.crmfoodestablishment.coreservice.dto.order.NewOrderDto;
import com.crmfoodestablishment.coreservice.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping("/order")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<UUID> createOrder(@RequestBody @Validated NewOrderDto newOrderDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderService.createOrder(newOrderDto));
    }
}