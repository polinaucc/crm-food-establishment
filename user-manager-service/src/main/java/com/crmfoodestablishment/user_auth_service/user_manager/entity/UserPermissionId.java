package com.crmfoodestablishment.user_auth_service.user_manager.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@RequiredArgsConstructor
public class UserPermissionId implements Serializable {

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Enumerated(EnumType.STRING)
    @Column(name = "permission", nullable = false)
    private Permission permission;
}
