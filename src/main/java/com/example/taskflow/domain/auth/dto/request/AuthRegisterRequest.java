package com.example.taskflow.domain.auth.dto.request;

public record AuthRegisterRequest(
        String username,
        String email,
        String password,
        String name
) {}
