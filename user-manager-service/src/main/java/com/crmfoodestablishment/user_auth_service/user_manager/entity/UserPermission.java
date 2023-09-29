package com.crmfoodestablishment.user_auth_service.user_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class UserPermission {

    @EmbeddedId
    private UserPermissionId userPermissionId;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("userId")
    private User user;
}
