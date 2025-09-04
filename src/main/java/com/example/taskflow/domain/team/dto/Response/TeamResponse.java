package com.example.taskflow.domain.team.dto.Response;

import com.example.taskflow.domain.team.entity.Team;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class TeamResponse {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private List<UserResponse> member;

    private TeamResponse(Team team) {
        this.id = team.getId();
        this.name = team.getName();
        this.description = team.getDescription();
        this.createdAt = team.getCreatedAt();
        this.member = team.getMember().stream().map(UserResponse::from).toList();
    }

    public static TeamResponse from(Team team) {
        return new TeamResponse(team);
    }
}
