package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.DishInOrder;
import com.crmfoodestablishment.coreservice.entity.DishOrderId;
import org.springframework.data.repository.CrudRepository;

public interface DishInOrderRepository extends CrudRepository<DishInOrder, DishOrderId> {
}