package com.example.taskflow.domain.team.repository;

import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.exception.InvalidTeamException;
import com.example.taskflow.domain.team.exception.TeamErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {
    default Team findByIdOrElseThrow(long id) {
        return findById(id).orElseThrow(() -> new InvalidTeamException(TeamErrorCode.TEAM_NOT_FOUND));
    }

    Optional<Team> findByName(String name);

    @Query("SELECT DISTINCT t FROM Team t LEFT JOIN FETCH t.member tm LEFT JOIN FETCH tm.user u WHERE t.id = :id")
    Optional<Team> findByTeamId(Long id);

    List<Team> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String nameKeyword, String descriptionKeyword);
}