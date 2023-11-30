package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import com.crmfoodestablishment.coreservice.entity.DishOrderId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishInOrderRepository extends JpaRepository<DishInOrder, DishOrderId> {
}