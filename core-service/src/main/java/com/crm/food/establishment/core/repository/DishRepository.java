package com.crm.food.establishment.core.repository;

import com.crm.food.establishment.core.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DishRepository extends JpaRepository<Dish, Integer> {
    boolean existsByName(String name);
}
