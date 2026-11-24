package com.example.rbac.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "roles")
@Getter @Setter @NoArgsConstructor
public class Role {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @Column(nullable = false, unique = true) private String name;
}
