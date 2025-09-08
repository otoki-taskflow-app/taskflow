package com.example.taskflow.domain.user.service;

import com.example.taskflow.domain.team.service.TeamExternalService;
import com.example.taskflow.domain.user.exception.UserErrorCode;
import com.example.taskflow.domain.user.dto.UserResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.exception.UserException;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserInternalService {

    private final UserRepository userRepository;
//    private final TeamExternalService teamExternalService;

    // 1. 현재 로그인한 사용자 정보 조회
    public UserResponse getMyInfo(Long userId) {

        User user = userRepository.findByIdAndDeletedAtIsNull(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.UNAUTHORIZED));

        return UserResponse.from(user);
    }


    // 2. 특정 팀 사용자 제외 조회
    public List<UserResponse> getUsersNotInTeam(Long teamId) {

        List<User> users = userRepository.findUsersNotInTeam(teamId);

        return users.stream()
                .map(UserResponse::from)
                .toList();
    }

    // 3. 전체 사용자 조회
    public List<UserResponse> getAllUsers() {

        List<User> users = userRepository.findAllByDeletedAtIsNull();

        return users.stream()
                .map(UserResponse::from)
                .toList();
    }

    // 4. 팀에 추가 가능한 사용자 목록 조회
    public List<UserResponse> getSelectableUsers(Long teamId) {
        if (teamId != null) {
//            팀 검증 포함할 시 (TeamExternalService에 코드 추가 필요)
//            teamExternalService.requireExisting(teamId);
            return getUsersNotInTeam(teamId);
        }

        return getAllUsers();
    }
}
