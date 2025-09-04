package com.example.taskflow.domain.auth.security;

import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import lombok.Getter;

@Getter
public class AuthUserDto {
    private final Long id;
    private final String username;
    private final String password;
    private final String email;
    private final Role role;

    private AuthUserDto(Long id, String username, String password, String email, Role role) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
    }

    public static AuthUserDto from(User user) {
        return new AuthUserDto(
                user.getId(),
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
    }
}
