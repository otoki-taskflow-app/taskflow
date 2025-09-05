package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamExternalService {
    private final TeamRepository teamRepository;

    // 팀 목록 조회
    @Transactional(readOnly = true)
    public List<TeamResponse> getTeamList() {
        List<Team> teamList = teamRepository.findAll();
        return teamList.stream().map(TeamResponse::from).toList();
    }

    //특정 팀 조회
    @Transactional(readOnly = true)
    public TeamResponse getTeamById(Long id) {
        Team team = teamRepository.findByIdOrElseThrow(id);
        return TeamResponse.from(team);
    }
}