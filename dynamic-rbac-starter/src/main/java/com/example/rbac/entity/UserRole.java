package com.example.rbac.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "user_roles", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"}))
@Getter @Setter @NoArgsConstructor
public class UserRole {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false) @JoinColumn(name = "user_id") private User user;
    @ManyToOne(optional = false) @JoinColumn(name = "role_id") private Role role;
    public UserRole(User user, Role role) { this.user = user; this.role = role; }
}
