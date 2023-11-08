package com.crmfoodestablishment.userauthservice.usermanager.dto;

import com.crmfoodestablishment.userauthservice.usermanager.entity.Role;
import com.crmfoodestablishment.userauthservice.usermanager.entity.UserPersonalInfo;
import lombok.*;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {

    private UUID uuid;

    private String email;

    private String password;

    private Set<Role> roles;

    private UserPersonalInfo personalInfo;
}
