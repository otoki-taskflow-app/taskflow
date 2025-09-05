package com.example.taskflow.domain.team.repository;

import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.exception.InvalidTeamException;
import com.example.taskflow.domain.team.exception.TeamErrorCode;
import jakarta.persistence.Entity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    default Team findByIdOrElseThrow(long id) {
        return findById(id).orElseThrow(() -> new InvalidTeamException(TeamErrorCode.TEAM_NOT_FOUND));
    }

    Optional<Team> findByName(String name);

    @EntityGraph(attributePaths = {"member", "member.user"}) //팀과 멤버, 유저를 한 번에 조회
    Optional<Team> findByIdWithMember(Long id);
}
