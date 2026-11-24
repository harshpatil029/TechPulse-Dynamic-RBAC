package com.example.rbac.controller;

import com.example.rbac.entity.User;
import com.example.rbac.entity.UserRole;
import com.example.rbac.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public User registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @PostMapping("/{userId}/roles/{roleId}")
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
    })
    @PreAuthorize("hasPermission(null, 'USER_ROLE_ASSIGN')")
    public UserRole assignRole(
            @PathVariable Long userId,
            @PathVariable Long roleId) {
        return userService.assignRole(userId, roleId);
    }
}
