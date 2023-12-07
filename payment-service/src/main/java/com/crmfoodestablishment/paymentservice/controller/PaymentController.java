package com.crmfoodestablishment.paymentservice.controller;

import com.crmfoodestablishment.paymentservice.dto.CreatePaymentDto;
import com.crmfoodestablishment.paymentservice.publisher.RabbitMQProducer;
import com.crmfoodestablishment.paymentservice.service.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private RabbitMQProducer producer;

    @PostMapping
    public ResponseEntity<CreatePaymentDto> createPayment(@Valid @RequestBody CreatePaymentDto paymentDto) {

        CreatePaymentDto responsePaymentDto = paymentService.addPayment(paymentDto);
        return new ResponseEntity<>(responsePaymentDto, HttpStatus.CREATED);
    }
}
