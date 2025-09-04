package com.example.taskflow.domain.team.dto.Response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeamMemberRequest {
    //팀 멤버 추가용
    private final Long userId;
}
