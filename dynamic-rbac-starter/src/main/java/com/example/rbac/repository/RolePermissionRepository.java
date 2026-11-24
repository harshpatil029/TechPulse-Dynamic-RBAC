package com.example.rbac.repository;
import com.example.rbac.entity.RolePermission;
import org.springframework.data.jpa.repository.JpaRepository;
public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    boolean existsByRoleIdAndPermissionId(Long roleId, Long permissionId);
}
