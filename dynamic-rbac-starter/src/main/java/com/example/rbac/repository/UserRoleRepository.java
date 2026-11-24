package com.example.rbac.repository;

import com.example.rbac.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
    @Query("select count(rp) > 0 from UserRole ur join RolePermission rp on rp.role = ur.role join rp.permission p where ur.user.username = :username and p.name = :permission")
    boolean userHasPermission(@Param("username") String username, @Param("permission") String permission);

    boolean existsByUserIdAndRoleId(Long userId, Long roleId);
}
