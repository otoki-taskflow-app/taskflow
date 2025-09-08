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
    @Query("""
        SELECT DISTINCT u
        FROM TeamMember tm
        JOIN tm.user u
        WHERE tm.team.id = :teamId
          AND tm.deletedAt IS NULL
          AND u.deletedAt IS NULL
    """)
    List<User> findActiveUsersByTeamId(Long teamId);

    // 3. 특정 팀에 속하지 않은 사용자 목록
    @Query("""
        SELECT u
        FROM User u
        LEFT JOIN TeamMember tm
            ON tm.user = u AND tm.team.id = :teamId AND tm.deletedAt IS NULL
        WHERE u.deletedAt IS NULL
          AND tm.id IS NULL
    """)
    List<User> findUsersNotInTeam(Long teamId);

    // 4. 전체 유저 조회
    List<User> findAllByDeletedAtIsNull();

    // 5. 관련 키워드로 유저 조회 (검색)
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String nameKeyword, String emailKeyword);
}
