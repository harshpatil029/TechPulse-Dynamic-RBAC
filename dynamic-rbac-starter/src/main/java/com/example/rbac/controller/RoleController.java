
package com.example.rbac.controller;

import com.example.rbac.entity.Permission;
import com.example.rbac.entity.Role;
import com.example.rbac.entity.RolePermission;
import com.example.rbac.service.RoleService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping
@SecurityRequirement(name = "bearerAuth")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }
    @PostMapping("/roles")
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PreAuthorize("hasPermission(null, 'ROLE_CREATE')")
    public Role createRole(@RequestBody Role role) {
        return roleService.createRole(role);
    }

    @PostMapping("/permissions")
    @PreAuthorize("hasPermission(null, 'PERMISSION_CREATE')")
    public Permission createPermission(@RequestBody Permission permission) {
        return roleService.createPermission(permission);
    }

    @PostMapping("/roles/{roleId}/permissions/{permissionId}")
    @PreAuthorize("hasPermission(null, 'ROLE_PERMISSION_ASSIGN')")
    public RolePermission assignPermission(
            @PathVariable Long roleId,
            @PathVariable Long permissionId) {
        return roleService.assignPermission(roleId, permissionId);
    }
}
