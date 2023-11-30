package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.DeliveryDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryDetailsRepository extends JpaRepository<DeliveryDetails, Integer> {
}
