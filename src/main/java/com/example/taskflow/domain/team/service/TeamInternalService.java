package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.dto.Request.TeamRequest;
import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.exception.InvalidTeamException;
import com.example.taskflow.domain.team.exception.TeamErrorCode;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamInternalService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    //팀 생성
    @Transactional
    public TeamResponse createTeam(TeamRequest request) {
        //팀이름 중복 확인
        if (teamRepository.findByName(request.getName()).isPresent()) {
            throw new InvalidTeamException(TeamErrorCode.TEAM_NAME_DUPLICATE);
        }

        Team team = new Team(request.getName(), request.getDescription());
        Team saveTeam = teamRepository.save(team);
        return TeamResponse.from(saveTeam);
    }

    //팀 멤버 추가
    @Transactional
    public TeamResponse createTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findByIdOrElseThrow(teamId);
        User user = userRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new InvalidTeamException(TeamErrorCode.USER_NOT_FOUND));
        //이미 멤버인지 확인
        boolean isMember = team.getMember().stream().anyMatch(u -> u.getId().equals(memberId));
        if (isMember) throw new InvalidTeamException(TeamErrorCode.TEAM_USER_DUPLICATE);
        team.addMember(user);
        teamRepository.save(team);
        return TeamResponse.from(team);
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
    public void deleteTeam(Long id) {
        Team team = teamRepository.findByIdOrElseThrow(id);
        teamRepository.delete(team);
    }

    //팀 멤버 삭제
    @Transactional
    public TeamResponse deleteTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findByIdOrElseThrow(teamId);
        User user = userRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new InvalidTeamException(TeamErrorCode.USER_NOT_FOUND));
        boolean isMember = team.getMember().stream().anyMatch(u -> u.getId().equals(memberId));
        if(!isMember) throw new InvalidTeamException(TeamErrorCode.USER_NOT_IN_TEAM);
        team.removeMember(user);
        return TeamResponse.from(team);
    }
}
