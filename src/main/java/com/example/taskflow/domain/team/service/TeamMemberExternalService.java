package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.dto.Response.TeamMemberResponse;
import com.example.taskflow.domain.team.entity.TeamMember;
import com.example.taskflow.domain.team.repository.TeamMemberRepository;
import com.example.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamMemberExternalService {
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    //팀 멤버 목록 조회
    @Transactional(readOnly = true)
    public List<TeamMemberResponse> getMemberByTeamId(Long teamId) {
        teamRepository.findByIdOrElseThrow(teamId);
        List<TeamMember> teamMembers = teamMemberRepository.findByTeamIdWithUsers(teamId);
        return teamMembers.stream().map(TeamMemberResponse::new).toList();
    }

    //추가 가능한 사용자 목록 조회
}
