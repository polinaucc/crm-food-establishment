package com.crmfoodestablishment.usermanager.crud.repository;

import com.crmfoodestablishment.usermanager.crud.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmail(String email);
}
