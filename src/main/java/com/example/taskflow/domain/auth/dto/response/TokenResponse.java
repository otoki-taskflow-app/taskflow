package com.example.taskflow.domain.auth.dto.response;

public record TokenResponse(String accessToken, String refreshToken, long refreshTime) {
    public static TokenResponse of(String accessToken, String refreshToken, long refreshTime) {
        return new TokenResponse(accessToken, refreshToken, refreshTime);
    }
}