package com.example.taskflow.domain.auth.dto.request;

public record AuthLoginRequest(
        String username,
        String password
) {}
