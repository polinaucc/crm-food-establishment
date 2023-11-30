package com.crmfoodestablishment.coreservice.service;

import com.crmfoodestablishment.coreservice.dto.CreateNewOrderDto;
import java.util.UUID;

public interface OrderService {

    UUID createOrder(CreateNewOrderDto createNewOrderDto);;
}