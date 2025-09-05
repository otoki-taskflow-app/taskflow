package com.example.taskflow.domain.auth.service;

import com.example.taskflow.domain.auth.dto.request.AuthLoginRequest;
import com.example.taskflow.domain.auth.dto.request.AuthRegisterRequest;
import com.example.taskflow.domain.auth.dto.response.AuthLoginResponse;
import com.example.taskflow.domain.auth.dto.response.AuthResponse;
import com.example.taskflow.domain.auth.exception.AuthErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.AuthRepository;
import com.example.taskflow.domain.auth.security.JwtProvider;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInternalService {

    private final AuthRepository authRepository;
    private final JwtProvider jwtProvider;

    @Transactional
    public AuthResponse signup(AuthRegisterRequest request) {

        if (authRepository.existsByUsername(request.username())) {
            throw new AuthException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }

        if (authRepository.existsByEmail(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
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

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = authRepository.findByUsername(request.username()).orElseThrow(() ->
                new AuthException(AuthErrorCode.INVALID_LOGIN));

        if (!ObjectUtils.nullSafeEquals(user.getPassword(), request.password())) {
            throw new AuthException(AuthErrorCode.INVALID_LOGIN);
        }

        String token = jwtProvider.createToken(user.getId(), user.getEmail(), user.getRole());

        return AuthLoginResponse.of(token);
    }
}
