package com.example.taskflow.domain.team.dto.Response;

import com.example.taskflow.domain.team.entity.Team;

public class TeamResponse {
    private String name;
    private String description;

    private TeamResponse(Team team){
        this.name = team.getName();
        this.description = team.getDescription();
    }

    public static TeamResponse of(Team team){return new TeamResponse(team);}
}
