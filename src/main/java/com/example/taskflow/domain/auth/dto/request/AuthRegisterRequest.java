package com.example.taskflow.domain.auth.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest(
        @Size(min = 4, max = 20)
        @Pattern(regexp = "^[a-zA-Z0-9]*$")
        String username,

        @Email
        String email,

        @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\[\\]{};':\"\\\\|,.<>/?]).{8,}$")
        String password,

        @Size(min = 2, max = 50)
        String name
) {}
