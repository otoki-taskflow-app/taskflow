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
    /*
     전제: JwtFilter에 다음 코드 추가 필요
     Long userId = jwtProvider.getUserId(token);  -> userId 추출
     request.setAttribute("userId", userId);
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(
            @RequestAttribute(value = "userId", required = false) Long userId) {

        UserResponse response = userInternalService.getMyInfo(userId);
        return ApiResponse.success(response, "사용자 정보를 조회했습니다.");
    }


    // 2. 팀에 추가 가능한 사용자 목록 조회
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAvailableUsers(@RequestParam("teamId") Long teamId) {

        List<UserResponse> responseList = userInternalService.getAvailableUsers(teamId);
        return ApiResponse.success(responseList, "사용 가능한 사용자 목록을 조회했습니다.");
    }
}
