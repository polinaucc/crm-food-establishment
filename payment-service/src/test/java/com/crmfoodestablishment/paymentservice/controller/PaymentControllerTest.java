package com.crmfoodestablishment.paymentservice.controller;

import com.crmfoodestablishment.paymentservice.dto.CreatePaymentDto;
import com.crmfoodestablishment.paymentservice.entity.Payment;
import com.crmfoodestablishment.paymentservice.entity.Status;
import com.crmfoodestablishment.paymentservice.service.PaymentServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PaymentController.class)
@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @MockBean
    PaymentServiceImpl paymentServiceImpl;

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    private Payment payment;
    private CreatePaymentDto requestPaymentDto;
    private CreatePaymentDto responsePaymentDto;

    @BeforeEach
    public void setUpData() {
        payment = new Payment();
        payment.setId(1);
        payment.setUuid(UUID.randomUUID());
        payment.setIsCash(false);
        payment.setSum(BigDecimal.valueOf(150.0));
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(Status.PAID);

        requestPaymentDto = new CreatePaymentDto();
        requestPaymentDto.setSum(BigDecimal.valueOf(150.0));
        requestPaymentDto.setIsCash(false);
        requestPaymentDto.setStatus(Status.PAID);

        responsePaymentDto = new CreatePaymentDto();
        responsePaymentDto.setUuid(payment.getUuid());
        requestPaymentDto.setPaymentDate(payment.getPaymentDate());
        responsePaymentDto.setStatus(Status.PAID);
        responsePaymentDto.setIsCash(false);
        requestPaymentDto.setSum(BigDecimal.valueOf(150.0));
    }

    @Test
    void shouldCreatePayment() throws Exception {
        when(paymentServiceImpl.addPayment(any(CreatePaymentDto.class)))
                .thenReturn(responsePaymentDto);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPaymentDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.uuid").value(responsePaymentDto.getUuid().toString()))
                .andExpect(jsonPath("$.isCash").value(responsePaymentDto.getIsCash()))
                .andExpect(jsonPath("$.sum").value(responsePaymentDto.getSum()))
                .andExpect(jsonPath("$.paymentDate").value(responsePaymentDto.getPaymentDate()))
                .andExpect(jsonPath("$.status").value(responsePaymentDto.getStatus().name()));
    }

    @Test
    void shouldThrow400BadRequestWhenRequestDtosNotValid() throws Exception {
        requestPaymentDto.setSum(null);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPaymentDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.currentTime").isNotEmpty())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("Validation failed"))
                .andExpect(jsonPath("$.errors['sum']").value("sum cannot be null"));
    }

    @Test
    void shouldGetValidInputThenMapsToBusinessModel() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/payment")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPaymentDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated());
        ArgumentCaptor<CreatePaymentDto> argumentCaptor = ArgumentCaptor.forClass(CreatePaymentDto.class);

        verify(paymentServiceImpl, times(1)).addPayment(argumentCaptor.capture());
        CreatePaymentDto paymentCaptorValue = argumentCaptor.getValue();
        assertEquals(payment.getSum(), paymentCaptorValue.getSum());
        assertEquals(payment.getStatus(), paymentCaptorValue.getStatus());
        assertEquals(payment.getIsCash(), paymentCaptorValue.getIsCash());
    }


}