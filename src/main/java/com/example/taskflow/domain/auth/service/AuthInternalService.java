package com.example.taskflow.domain.auth.service;

import com.example.taskflow.common.exception.CommonErrorCode;
import com.example.taskflow.domain.auth.dto.request.AuthLoginRequest;
import com.example.taskflow.domain.auth.dto.request.AuthRegisterRequest;
import com.example.taskflow.domain.auth.dto.request.AuthWithdrawRequest;
import com.example.taskflow.domain.auth.dto.response.AuthLoginResponse;
import com.example.taskflow.domain.auth.dto.response.AuthResponse;
import com.example.taskflow.domain.auth.dto.response.TokenResponse;
import com.example.taskflow.domain.auth.entity.RefreshToken;
import com.example.taskflow.domain.auth.exception.AuthErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.AuthRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInternalService {

    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenInternalService tokenInternalService;

    @Transactional
    public AuthResponse signup(AuthRegisterRequest request) {

        if (authRepository.existsByUsername(request.username())) {
            throw new AuthException(AuthErrorCode.USERNAME_ALREADY_EXISTS);
        }

        if (authRepository.existsByEmail(request.email())) {
            throw new AuthException(AuthErrorCode.EMAIL_ALREADY_EXISTS);
        }

        String encodePw = passwordEncoder.encode(request.password());

        User user = User.create(
                request.username(),
                encodePw,
                request.email(),
                request.name(),
                Role.USER
        );

        User savedUser = authRepository.save(user);

        return AuthResponse.from(savedUser);
    }

    @Transactional
    public AuthLoginResponse login(AuthLoginRequest request) {
        User user = authRepository.findByUsernameAndDeletedAtIsNull(request.username()).orElseThrow(() ->
                new AuthException(AuthErrorCode.USER_WITHDRAWN));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.INVALID_LOGIN);
        }

        String accessToken = tokenInternalService.generateOrUpdateTokens(user);

        return AuthLoginResponse.of(accessToken);
    }

    @Transactional
    public void logout(Long userId) {
        tokenInternalService.logout(userId);
    }

    @Transactional
    public void withdraw(AuthWithdrawRequest request, Long userId) {

        User user = authRepository.findById(userId).orElseThrow(
                () -> new AuthException(CommonErrorCode.INVALID_USER));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AuthException(AuthErrorCode.PASSWORD_MISSMATCH);
        }

        user.setDeletedAt(LocalDateTime.now());
    }

}
