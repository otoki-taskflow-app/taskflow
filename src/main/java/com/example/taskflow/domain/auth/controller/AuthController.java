package com.example.taskflow.domain.auth.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.auth.dto.request.AuthLoginRequest;
import com.example.taskflow.domain.auth.dto.request.AuthRegisterRequest;
import com.example.taskflow.domain.auth.dto.response.AuthLoginResponse;
import com.example.taskflow.domain.auth.dto.response.AuthResponse;
import com.example.taskflow.domain.auth.service.AuthInternalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthInternalService authInternalService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AuthResponse>> signup(
            @RequestBody AuthRegisterRequest request
    ) {
        AuthResponse response = authInternalService.signup(request);

        return ApiResponse.created(response, "회원가입이 완료되었습니다.");
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthLoginResponse>> login(
            @RequestBody AuthLoginRequest request
    ) {
        AuthLoginResponse response = authInternalService.login(request);

        return ApiResponse.created(response, "로그인이 완료되었습니다.");
    }
}
