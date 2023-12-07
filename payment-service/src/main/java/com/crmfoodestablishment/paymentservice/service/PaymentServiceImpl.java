package com.crmfoodestablishment.paymentservice.service;

import com.crmfoodestablishment.paymentservice.dto.CreatePaymentDto;
import com.crmfoodestablishment.paymentservice.entity.Payment;
import com.crmfoodestablishment.paymentservice.mapper.PaymentMapper;
import com.crmfoodestablishment.paymentservice.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    private final PaymentMapper paymentMapper = Mappers.getMapper(PaymentMapper.class);

    @Override
    public CreatePaymentDto addPayment(CreatePaymentDto paymentDto) {
        Payment paymentToSave = paymentMapper.mapCreatePaymentDtoToPayment(paymentDto);
        Payment savedPayment = paymentRepository.save(paymentToSave);
        return paymentMapper.mapPaymentToCreatePaymentDto(savedPayment);
    }
}
