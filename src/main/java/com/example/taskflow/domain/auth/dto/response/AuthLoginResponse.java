package com.example.taskflow.domain.auth.dto.response;

public record AuthLoginResponse(
        String token
) {
    public static AuthLoginResponse of(String token) {
        return new AuthLoginResponse(token);
    }
}
