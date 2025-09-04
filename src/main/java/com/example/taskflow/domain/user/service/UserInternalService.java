package com.example.taskflow.domain.user.service;

import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInternalService {

    private final UserRepository userRepository;


    // 1. 단일 유저 조회 (탈퇴한 유저 제외)
    @Transactional(readOnly = true)
    public Optional<User> findByIdAndDeletedAtIsNull(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id);
    }

    // 2. 팀에 속하지 않은 유저 목록 조회 (탈퇴한 유저 제외)
    @Transactional(readOnly = true)
    public List<User> findAvailableUsersByTeamId(Long teamId) {
        return userRepository.findAvailableUsersByTeamId(teamId);
    }
}
