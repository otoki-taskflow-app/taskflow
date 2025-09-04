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

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInternalService {

    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    // 1. 현재 로그인한 사용자 정보 조회
    public UserResponse getMyInfo(Long userId) {

        if (userId == null) {
            throw new GlobalException(UserErrorCode.UNAUTHORIZED);
        }

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new GlobalException(UserErrorCode.USER_NOT_FOUND));

        return new UserResponse(user);
    }


    // 2. 팀에 추가 가능한 사용자 목록 조회
    public List<UserResponse> getAvailableUsers(Long teamId) {

        if (!teamRepository.existsById(teamId)) {
            throw new GlobalException(UserErrorCode.TEAM_NOT_FOUND); }

        List<User> users = userRepository.findAvailableUsersByTeamId(teamId);

        List<UserResponse> responseList = new ArrayList<>();
        for (User user : users) {
            responseList.add(new UserResponse(user));
        }
        return responseList;
    }
}
