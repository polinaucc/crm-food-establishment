package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface MenuRepository extends JpaRepository<Menu, UUID> {

    boolean existsByName(String name);

    Optional<Menu> getMenuByUuid(UUID uuid);
}
