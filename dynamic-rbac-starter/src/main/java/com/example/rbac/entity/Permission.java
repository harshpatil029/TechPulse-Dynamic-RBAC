package com.example.rbac.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "permissions")
@Getter @Setter @NoArgsConstructor
public class Permission {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
}
