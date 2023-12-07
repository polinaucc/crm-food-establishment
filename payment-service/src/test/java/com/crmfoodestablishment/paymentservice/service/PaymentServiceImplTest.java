package com.crmfoodestablishment.paymentservice.service;

import com.crmfoodestablishment.paymentservice.dto.CreatePaymentDto;
import com.crmfoodestablishment.paymentservice.entity.Payment;
import com.crmfoodestablishment.paymentservice.entity.Status;
import com.crmfoodestablishment.paymentservice.mapper.PaymentMapper;
import com.crmfoodestablishment.paymentservice.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @InjectMocks
    private PaymentServiceImpl paymentServiceImpl;

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PaymentMapper paymentMapper;

    private Payment payment;

    private CreatePaymentDto createPaymentDto;

    @BeforeEach
    public void setUpData() {
        payment = new Payment();
        payment.setId(1);
        payment.setUuid(UUID.randomUUID());
        payment.setIsCash(false);
        payment.setSum(BigDecimal.valueOf(150.0));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(Status.PAID);

        createPaymentDto = new CreatePaymentDto();
        createPaymentDto.setSum(BigDecimal.valueOf(150.0));
        createPaymentDto.setIsCash(false);
        createPaymentDto.setStatus(Status.PAID);
    }

    @Test
    void shouldAddNewPayment() {
        ArgumentCaptor<Payment> argumentCaptor = ArgumentCaptor.forClass(Payment.class);
        when(paymentRepository.save(any())).thenReturn(payment);
        paymentServiceImpl.addPayment(createPaymentDto);

        verify(paymentRepository, times(1)).save(argumentCaptor.capture());
        Payment paymentCaptorValue = argumentCaptor.getValue();
        assertEquals(payment.getSum(), paymentCaptorValue.getSum());
        assertEquals(payment.getStatus(), paymentCaptorValue.getStatus());
        assertEquals(payment.getIsCash(), paymentCaptorValue.getIsCash());
    }

    @Test
    void shouldPaymentFieldsMatchPaymentDtoFields() {
        when(paymentRepository.save(any())).thenReturn(payment);
        paymentServiceImpl.addPayment(createPaymentDto);

        assertThat(createPaymentDto.getSum()).isEqualTo(payment.getSum());
        assertThat(createPaymentDto.getStatus()).isEqualTo(payment.getStatus());
        assertThat(createPaymentDto.getIsCash()).isEqualTo(payment.getIsCash());
    }
}
