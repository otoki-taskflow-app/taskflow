package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.dto.Request.TeamRequest;
import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.exception.InvalidTeamException;
import com.example.taskflow.domain.team.exception.TeamErrorCode;
import com.example.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamInternalService {
    private final TeamRepository teamRepository;
    //팀 생성
    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        //팀이름 중복 확인
        if (teamRepository.findByName(request.getName()).isPresent()) {
            throw new InvalidTeamException(TeamErrorCode.TEAM_NAME_DUPLICATE);
        }
        Team team = new Team(request.getName(), request.getDescription());
        Team savedTeam = teamRepository.save(team);
        return TeamResponse.from(savedTeam);
    }

    //팀 정보 수정
    @Transactional
    public TeamResponse updateTeam(Long id, TeamRequest request) {
        Team team = teamRepository.findByIdOrElseThrow(id);
        team.updateTeam(request.getName(), request.getDescription());
        Team savedTeam = teamRepository.save(team);//명시적 저장
        return TeamResponse.from(savedTeam);
    }

    //팀 삭제
    @Transactional
    public void deleteTeam(Long id) {
        Team team = teamRepository.findByIdOrElseThrow(id);
        teamRepository.delete(team);
    }
}
