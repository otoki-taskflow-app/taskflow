package com.example.taskflow.domain.search.dto;

import com.example.taskflow.domain.team.entity.Team;

public record TeamSummary(
        Long id,
        String name,
        String description
) {
    public static TeamSummary from(Team team) {
        return new TeamSummary(
                team.getId(),
                team.getName(),
                team.getDescription()
        );
    }
}