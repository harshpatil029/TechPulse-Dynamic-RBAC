package com.example.rbac.dto;

public record LoginResponse(String token, String username, String role) {}