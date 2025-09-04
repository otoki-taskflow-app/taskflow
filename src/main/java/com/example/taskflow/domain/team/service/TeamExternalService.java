package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.dto.Request.TeamRequest;
import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.exception.InvalidTeamException;
import com.example.taskflow.domain.team.exception.TeamErrorCode;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.dto.UserResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamExternalService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

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

    //팀 멤버 조회
    @Transactional(readOnly = true)
    public List<UserResponse> getMemberByTeamId(Long teamId) {
        Team team = teamRepository.findByIdOrElseThrow(teamId);
        return team.getMember().stream().map(UserResponse::new).collect(Collectors.toList());
    }
}