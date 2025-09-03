package com.example.taskflow.domain.auth.service;

import com.example.taskflow.domain.auth.dto.request.AuthRegisterRequest;
import com.example.taskflow.domain.auth.dto.response.AuthResponse;
import com.example.taskflow.domain.auth.exception.AuthErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.AuthRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInternalService {

    private final AuthRepository authRepository;

    public AuthResponse signup(AuthRegisterRequest request) {

        if (authRepository.existsByUsername(request.username())) {
            throw new AuthException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }

        // TODO: 비밀번호 인코딩

        User user = User.create(
                request.username(),
                request.password(),
                request.email(),
                request.name(),
                Role.user
        );

        User savedUser = authRepository.save(user);

        return AuthResponse.from(savedUser);
    }
}
