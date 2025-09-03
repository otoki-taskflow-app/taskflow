package com.example.taskflow.domain.team.repository;


import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.team.entity.Team;
import com.example.taskflow.domain.team.exception.TeamErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
    default Team findByIdOrElseThrow(long id) {
        return findById(id).orElseThrow(() -> new InvalidTaskException(TeamErrorCode.TEAM_NOT_FOUND));
    }
}
