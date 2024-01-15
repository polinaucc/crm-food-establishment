package com.crm.food.establishment.user.manager.dto;

import com.crm.food.establishment.user.manager.entity.Role;

import java.time.LocalDate;
import java.util.UUID;

public record UserDto(
        UUID uuid,
        String email,
        Role role,
        String firstName,
        String lastName,
        boolean isMale,
        LocalDate birthday,
        String address
) {
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDto userDTO)) return false;

        return uuid.equals(userDTO.uuid);
    }

    @Override
    public int hashCode() {
        return uuid != null ? uuid.hashCode() : 0;
    }
}
