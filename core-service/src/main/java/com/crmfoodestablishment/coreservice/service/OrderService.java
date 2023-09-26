package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.NewOrderDto;

import java.util.UUID;

public interface OrderService {

    UUID createOrder(NewOrderDto newOrderDto);;
}