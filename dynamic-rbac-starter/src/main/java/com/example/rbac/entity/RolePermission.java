package com.example.rbac.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "role_permissions", uniqueConstraints = @UniqueConstraint(columnNames = {"role_id", "permission_id"}))
@Getter @Setter @NoArgsConstructor
public class RolePermission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "role_id") private Role role;
    @ManyToOne(optional = false) @JoinColumn(name = "permission_id") private Permission permission;
    public RolePermission(Role role, Permission permission) { this.role = role; this.permission = permission; }
}
