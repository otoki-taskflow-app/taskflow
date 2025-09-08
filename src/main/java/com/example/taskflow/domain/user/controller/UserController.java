package com.example.taskflow.domain.user.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.user.dto.UserResponse;
import com.example.taskflow.domain.user.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInternalService userInternalService;

    // 1. 현재 로그인한 사용자 정보 조회
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(@CurrentUser Long userId) {

        UserResponse response = userInternalService.getMyInfo(userId);
        return ApiResponse.success(response, "사용자 정보를 조회했습니다.");
    }


    // 2. 팀에 추가 가능한 사용자 목록 조회
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAvailableUsers(
            @RequestParam(value = "teamId", required = false) Long teamId) {

        List<UserResponse> responseList = userInternalService.getSelectableUsers(teamId);

        return ApiResponse.success(responseList, "사용 가능한 사용자 목록을 조회했습니다.");
    }
}
