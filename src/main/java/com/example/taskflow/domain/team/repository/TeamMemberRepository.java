package com.example.taskflow.domain.team.repository;

import com.example.taskflow.domain.team.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    @Query("SELECT tm FROM TeamMember tm JOIN FETCH tm.user WHERE tm.team.id = :teamId")
    List<TeamMember> findByTeamIdWithUsers(@Param("teamId") Long teamId);
}