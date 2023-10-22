package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.Dish;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

public interface DishRepository extends CrudRepository<Dish, Integer> {

    Optional<Dish> findByUuid(UUID uuid);
}
