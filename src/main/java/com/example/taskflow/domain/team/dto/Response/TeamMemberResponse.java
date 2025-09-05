package com.example.taskflow.domain.team.dto.Response;

import com.example.taskflow.domain.team.entity.TeamMember;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamMemberResponse {
    private final Long id;
    private final String username;
    private final String name;
    private final String email;
    private final Role role;
    private final LocalDateTime createdAt;

    public TeamMemberResponse(TeamMember teamMember) {
        User user = teamMember.getUser();
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
    public TeamMemberResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.name = user.getName();
        this.email = user.getEmail();
        this.role = user.getRole();
        this.createdAt = user.getCreatedAt();
    }
}