package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    boolean existsByName(String name);
}
