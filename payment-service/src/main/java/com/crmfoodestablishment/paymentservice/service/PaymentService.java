package com.crmfoodestablishment.paymentservice.service;

import com.crmfoodestablishment.paymentservice.dto.CreatePaymentDto;

public interface PaymentService {

    CreatePaymentDto addPayment(CreatePaymentDto paymentDto);
}
