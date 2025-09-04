package com.example.taskflow.domain.user.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.user.Error.UserErrorCode;
import com.example.taskflow.domain.user.dto.UserResponse;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import com.example.taskflow.domain.user.service.UserInternalService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserInternalService userInternalService;
//    private final TeamRepository teamRepository;


    /*
    1. 현재 로그인한 사용자 정보 조회
    // 전제: JwtFilter에 다음 코드 추가 필요
    // Long userId = jwtProvider.getUserId(token);  -> userId 추출
    // request.setAttribute("userId", userId);
     */
    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getMyInfo(@RequestAttribute("userId") Long userId) {

        if (userId == null) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(ApiResponse.error(UserErrorCode.UNAUTHORIZED));
        }

        Optional<User> optionalUser = userInternalService.findByIdAndDeletedAtIsNull(userId);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserResponse response = new UserResponse(user);
            return ApiResponse.success(response, "사용자 정보를 조회했습니다");
        } else {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(ApiResponse.error(UserErrorCode.USER_NOT_FOUND));
        }
    }


    // 2. 팀에 추가 가능한 사용자 목록 조회
    @GetMapping("/available")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAvailableUsers(@RequestParam("teamId") Long teamId) {

        // 전제: teamRepository 접근 필요
//        if(!teamRepository.existById(teamId)) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body(ApiResponse.error(UserErrorCode.TEAM_NOT_FOUND));
//        }

        List<User> users = userInternalService.findAvailableUsersByTeamId(teamId);

        List<UserResponse> responseList = new ArrayList<>();
        for (User user : users) {
            responseList.add(new UserResponse(user));
        }

        return ApiResponse.success(responseList, "사용 가능한 사용자 목록을 조회했습니다.");
    }
}
