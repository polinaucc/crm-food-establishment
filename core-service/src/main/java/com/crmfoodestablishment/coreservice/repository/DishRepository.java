package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.Dish;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface DishRepository extends JpaRepository<Dish, Integer> {

    Optional<Dish> findByUuid(UUID uuid);
}
