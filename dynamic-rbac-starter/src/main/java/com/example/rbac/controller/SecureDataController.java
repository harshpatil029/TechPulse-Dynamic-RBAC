package com.example.rbac.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class SecureDataController {

    @GetMapping("/secure-data")
    @Operation(security = {
            @SecurityRequirement(name = "bearerAuth")
    })
  @PreAuthorize("hasPermission(null, 'SECURE_DATA_READ')")
    public String getSecureData() {
        return "You have permission to access secure data.";
    }
}