package com.crmfoodestablishment.coreservice.repository;

import com.crmfoodestablishment.coreservice.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Integer> {

}