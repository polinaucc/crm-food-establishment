package com.crmfoodestablishment.user_auth_service.user_manager.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    private Integer id;

    @Column(name = "password", length = 32)
    private String password;

    @Column(name = "email", unique = true, length = 64)
    private String email;

    @Column(name = "uuid", columnDefinition = "BINARY")
    @JdbcTypeCode(SqlTypes.BINARY)
    private UUID uuid;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private List<UserPermission> userPermissionList = new ArrayList<>();

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    private UserPersonalInfo userPersonalInfo;

    public List<Permission> getPermissionsList() {
        List<Permission> permissionList = new ArrayList<>();

        this.getUserPermissionList()
                .forEach(
                        userPermission -> permissionList.add(userPermission.getUserPermissionId().getPermission())
                );

        return permissionList;
    }
}
