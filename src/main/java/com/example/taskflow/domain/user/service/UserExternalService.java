package com.example.taskflow.domain.user.service;


import com.example.taskflow.common.exception.GlobalException;
import com.example.taskflow.domain.team.repository.TeamRepository;
import com.example.taskflow.domain.user.Error.UserErrorCode;
import com.example.taskflow.domain.user.dto.UserResponse;
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
    private final TeamRepository teamRepository;


    // 1. 단일 유저 조회 (탈퇴한 유저 제외)
    public UserResponse getFindById(Long id) {
        User user = userRepository.findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));
        return new UserResponse(user);
    }

    // 2. 여러 유저 조회 (탈퇴한 유저 제외)
    // 팀 멤버 목록 조회 시 사용
    public List<UserResponse> getActiveMemberByTeamId(Long teamId) {
        if (!teamRepository.existsById(teamId)) {
            throw new GlobalException(UserErrorCode.TEAM_NOT_FOUND);
        }

        List<User> users = userRepository.findAllByTeamIdAndDeletedAtIsNull(teamId);

        if (users.isEmpty()) {
            throw new GlobalException(UserErrorCode.USER_NOT_FOUND);
        }

        return users.stream()
                .map(UserResponse::new)
                .toList();
    }

}
