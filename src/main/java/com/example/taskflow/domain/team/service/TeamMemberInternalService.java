package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.entity.TeamMember;
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
public class TeamMemberInternalService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    //팀 멤버 추가
    @Transactional
    public TeamResponse createTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new InvalidTeamException(TeamErrorCode.TEAM_NOT_FOUND));
        User user = userRepository.findByIdAndDeletedAtIsNull(memberId)
                .orElseThrow(() -> new InvalidTeamException(TeamErrorCode.USER_NOT_FOUND));

        boolean isMember = team.getMember().stream().anyMatch(tm -> tm.getUser().getId().equals(memberId));
        if (isMember) throw new InvalidTeamException(TeamErrorCode.TEAM_USER_DUPLICATE);
        TeamMember teamMember = new TeamMember(team, user);
        team.addMember(teamMember);
        teamRepository.save(team);//명시적 저장
        return TeamResponse.from(team);
    }

    //팀 멤버 삭제
    @Transactional
    public TeamResponse deleteTeamMember(Long teamId, Long memberId) {
        Team team = teamRepository.findByTeamId(teamId)
                .orElseThrow(() -> new InvalidTeamException(TeamErrorCode.TEAM_NOT_FOUND));

        TeamMember teamMemberToRemove = team.getMember().stream()
                .filter(tm -> tm.getUser().getId().equals(memberId))
                .findFirst()//스트림에서 조건을 만족하는 첫번쨰 요소를 찾음
                .orElseThrow(() -> new InvalidTeamException(TeamErrorCode.USER_NOT_IN_TEAM));

        team.removeMember(teamMemberToRemove);
        teamRepository.save(team);//명시적 저장
        return TeamResponse.from(team);
    }
}
