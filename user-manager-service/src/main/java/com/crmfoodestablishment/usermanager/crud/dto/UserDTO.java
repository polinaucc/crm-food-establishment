package com.crmfoodestablishment.usermanager.crud.dto;

import com.crmfoodestablishment.usermanager.crud.entity.Role;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID uuid;

    private String email;

    private Role role;

    private String firstName;

    private String lastName;

    private boolean isMale;

    private LocalDate birthday;

    private String address;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserDTO teacher)) return false;

        return getUuid().equals(teacher.getUuid());
    }

    @Override
    public int hashCode() {
        return getUuid().hashCode();
    }
}
