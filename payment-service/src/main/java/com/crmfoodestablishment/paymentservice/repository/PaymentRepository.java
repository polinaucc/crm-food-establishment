package com.crmfoodestablishment.paymentservice.repository;

import com.crmfoodestablishment.paymentservice.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

}
