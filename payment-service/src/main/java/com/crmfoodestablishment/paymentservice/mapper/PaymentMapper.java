package com.crmfoodestablishment.paymentservice.mapper;

import com.crmfoodestablishment.paymentservice.dto.CreatePaymentDto;
import com.crmfoodestablishment.paymentservice.entity.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface PaymentMapper {

    CreatePaymentDto mapPaymentToCreatePaymentDto(Payment payment);

    @Mapping(target = "uuid", expression = "java(java.util.UUID.randomUUID())")
    @Mapping(target = "paymentDate", expression = "java(java.time.LocalDateTime.now())")
    Payment mapCreatePaymentDtoToPayment(CreatePaymentDto paymentDto);
}
