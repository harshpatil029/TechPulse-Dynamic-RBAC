
package com.example.rbac.service;

import com.example.rbac.entity.Permission;
import com.example.rbac.entity.Role;
import com.example.rbac.entity.RolePermission;
import com.example.rbac.repository.PermissionRepository;
import com.example.rbac.repository.RolePermissionRepository;
import com.example.rbac.repository.RoleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final RolePermissionRepository rolePermissionRepository;

    public RoleService(
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            RolePermissionRepository rolePermissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.rolePermissionRepository = rolePermissionRepository;
    }

    public Role createRole(Role role) {
        if (roleRepository.existsByName(role.getName())) {
            throw new IllegalArgumentException("Role already exists");
        }

        return roleRepository.save(role);
    }

    public Permission createPermission(Permission permission) {
        if (permissionRepository.existsByName(permission.getName())) {
            throw new IllegalArgumentException("Permission already exists");
        }

        return permissionRepository.save(permission);
    }

    @Transactional
    public RolePermission assignPermission(Long roleId, Long permissionId) {
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));

        Permission permission = permissionRepository.findById(permissionId)
                .orElseThrow(() -> new IllegalArgumentException("Permission not found"));

        boolean alreadyAssigned =
                rolePermissionRepository.existsByRoleIdAndPermissionId(
                        roleId, permissionId);

        if (alreadyAssigned) {
            throw new IllegalArgumentException("Permission already assigned to role");
        }

        RolePermission rolePermission = new RolePermission();
        rolePermission.setRole(role);
        rolePermission.setPermission(permission);

        return rolePermissionRepository.save(rolePermission);
    }
}
