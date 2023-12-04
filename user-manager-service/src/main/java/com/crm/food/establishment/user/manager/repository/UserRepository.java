package com.crm.food.establishment.user.manager.repository;

import com.crm.food.establishment.user.manager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmail(String email);
}
