package com.example.taskflow.domain.search.dto;

import java.util.List;

public record SearchResponse (
    List<TaskSummary> tasks,
    List<UserSummary> users,
    List<TeamSummary> teams
) {}
