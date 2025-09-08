package com.example.taskflow.domain.user.service;


import com.example.taskflow.common.exception.GlobalException;
import com.example.taskflow.domain.search.dto.UserSummary;
import com.example.taskflow.domain.user.exception.UserErrorCode;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserExternalService {

    private final UserRepository userRepository;


    // 1. 단일 유저 조회 (탈퇴한 유저 제외)
    public User getUserById(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));
    }

    // 2. 여러 유저 조회 (탈퇴한 유저 제외)
    // 팀 멤버 목록 조회 시 사용
    public List<User> getActiveMemberByTeamId(Long teamId) {
        List<User> users = userRepository.findActiveUsersByTeamId(teamId);

        if (users.isEmpty()) {
            throw new GlobalException(UserErrorCode.USER_NOT_FOUND);
        }

        return users;
    }

    // 3. 관련 키워드로 유저 조회
    public List<UserSummary> searchByKeyword(String keyword) {
        return userRepository
                .findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(UserSummary::from)
                .toList();
    }

}
