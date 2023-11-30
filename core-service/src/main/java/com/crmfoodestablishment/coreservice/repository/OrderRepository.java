package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {
}