package com.example.taskflow.domain.team.dto.Response;

import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class TeamMemberResponse {
    private Long id;
    private String username;
    private String name;
    private String email;
    private Role role;
    private LocalDateTime createdAt;

    public TeamMemberResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}