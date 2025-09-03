package com.example.taskflow.domain.team.service;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.team.dto.Request.TeamRequest;
import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamExternalService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    //팀 생성
    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        Team team = new Team(request.getName(), request.getDescription());
        Team saveTeam = teamRepository.save(team);
        return TeamResponse.from(saveTeam);
    }

    //팀 멤버 추가
    public TeamResponse createTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findByIdOrElseThrow(teamId);
        User user = userRepository.findByIdOrElseThrow(memberId);
        team.addMember(user);
        teamRepository.save(team);
        return TeamResponse.from(team);
    }

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
        return team.getMember().stream().map(UserResponse::from).collect(Collectors.toList());
    }

    //팀 정보 수정
    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team team = teamRepository.findByIdOrElseThrow(id);
        team.updateTeam(request.getName(), request.getDescription());
        return TeamResponse.from(team);
    }

    //팀 삭제
    @Transactional
    public void deleteTeam(Long id, TeamRequest request) {
        Team team = teamRepository.findByIdOrElseThrow(id);
        teamRepository.delete(team);
    }

    //팀 멤버 삭제
    @Transactional
    public void deleteTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findByIdOrElseThrow(teamId);
        User user = userRepository.findByIdOrElseThrow(memberId);
        team.removeMember(user);
        teamRepository.save(team);
    }
}
