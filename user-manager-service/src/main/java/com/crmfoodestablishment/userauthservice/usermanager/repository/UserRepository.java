package com.crmfoodestablishment.userauthservice.usermanager.repository;

import com.crmfoodestablishment.userauthservice.usermanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUuid(UUID uuid);

    Optional<User> findByEmail(String email);
}
