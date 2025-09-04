package com.example.taskflow.domain.user.repository;

import com.example.taskflow.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    // 1. 현재 로그인한 사용자 정보 조회 (deletedAt이 null인 유저만)
    Optional<User> findByIdAndDeletedAtIsNull(Long id);

    // 2. 특정 팀에 속한 사용자 목록
    List<User> findAllByTeamIdAndDeletedAtIsNull(Long teamId);

    // 3. 팀에 추가 가능한 사용자 목록 조회
    @Query("""
        SELECT u
        FROM User u
        WHERE u.deletedAt IS NULL
          AND u.id NOT IN (
              SELECT tm.user.id
              FROM TeamMember tm
              WHERE tm.team.id = :teamId
          )
        """)
    List<User> findAvailableUsersByTeamId(Long teamId);
}
