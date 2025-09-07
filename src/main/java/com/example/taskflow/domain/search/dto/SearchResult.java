package com.example.taskflow.domain.search.dto;

import java.util.List;

public record SearchResult(
    List<TaskSummary> tasks,
    List<UserSummary> users,
    List<TeamSummary> teams
) {

    public static SearchResult of(List<TaskSummary> tasks, List<UserSummary> users, List<TeamSummary> teams) {
        return new SearchResult(tasks, users, teams);
    }
}
