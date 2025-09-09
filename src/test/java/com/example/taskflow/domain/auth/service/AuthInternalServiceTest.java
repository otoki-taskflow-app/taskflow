package com.example.taskflow.domain.auth.service;

import com.example.taskflow.common.exception.CommonErrorCode;
import com.example.taskflow.domain.auth.dto.request.AuthLoginRequest;
import com.example.taskflow.domain.auth.dto.request.AuthRegisterRequest;
import com.example.taskflow.domain.auth.dto.request.AuthWithdrawRequest;
import com.example.taskflow.domain.auth.dto.response.AuthLoginResponse;
import com.example.taskflow.domain.auth.dto.response.AuthResponse;
import com.example.taskflow.domain.auth.dto.response.TokenResponse;
import com.example.taskflow.domain.auth.exception.AuthErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.AuthRepository;
import com.example.taskflow.domain.auth.security.JwtProvider;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class AuthInternalServiceTest {

    @Mock
    private AuthRepository authRepository;

    @Mock
    private JwtProvider jwtProvider;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private TokenInternalService tokenInternalService;

    @InjectMocks
    private AuthInternalService authInternalService;

    @Test
    void 회원가입에_성공한다() {
        // given
        AuthRegisterRequest request = new AuthRegisterRequest("name", "Password!", "email@example.coim", "홍길동");

        given(authRepository.existsByUsername(request.username())).willReturn(false);
        given(authRepository.existsByEmail(request.email())).willReturn(false);
        given(passwordEncoder.encode(request.password())).willReturn("encodedPassword");

        User user = User.create(request.username(), "encodedPassword", request.email(), request.name(), Role.USER);
        given(authRepository.save(any(User.class))).willReturn(user);

        // when
        AuthResponse response = authInternalService.signup(request);

        // then
        assertEquals(request.username(), response.username());
        assertEquals(request.email(), response.email());
        assertEquals(request.name(), response.name());
        assertEquals(Role.USER.name(), response.role());
        verify(authRepository, times(1)).save(any(User.class));
    }

    @Test
    void 회원가입_username이_중복이면_AuthException을_반환한다() {
        // given
        AuthRegisterRequest request = new AuthRegisterRequest("name", "Password!", "email@example.coim", "홍길동");
        given(authRepository.existsByUsername(request.username())).willReturn(true);

        // when
        AuthException exception = assertThrows(AuthException.class,
                () -> authInternalService.signup(request));

        // then
        assertEquals(AuthErrorCode.USERNAME_ALREADY_EXISTS, exception.getErrorCode());
    }

    @Test
    void 회원가입_email이_중복이면_AuthException을_반환한다() {
        // given
        AuthRegisterRequest request = new AuthRegisterRequest("name", "Password!", "email@example.coim", "홍길동");
        given(authRepository.existsByUsername(request.username())).willReturn(false);
        given(authRepository.existsByEmail(request.email())).willReturn(true);

        // when
        AuthException exception = assertThrows(AuthException.class,
                () -> authInternalService.signup(request));

        // then
        assertEquals(AuthErrorCode.EMAIL_ALREADY_EXISTS, exception.getErrorCode());
    }
    @Test
    void 로그인에_성공하면_jwt토큰을_반환한다() {
        // given
        AuthLoginRequest request = new AuthLoginRequest("name", "Password!");
        User user = User.create("name", "encodedPassword", "email@example.coim", "홍길동", Role.USER);

        given(authRepository.findByUsernameAndDeletedAtIsNull(request.username()))
                .willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(true);

        TokenResponse mockedTokenResponse =
                TokenResponse.of("mockedAccessToken", "mockedRefreshToken", 3600000L);

        given(tokenInternalService.generateOrUpdateTokens(user))
                .willReturn("mockedJwtToken");

        // when
        AuthLoginResponse response = authInternalService.login(request);

        // then
        assertEquals("mockedJwtToken", response.token());
        verify(tokenInternalService, times(1)).generateOrUpdateTokens(user); // 실제 호출 확인
    }



    @Test
    void 로그인_username가_없는_경우_AuthException을_반환한다() {
        // given
        AuthLoginRequest request = new AuthLoginRequest("name", "Password!");
        given(authRepository.findByUsernameAndDeletedAtIsNull(request.username())).willReturn(Optional.empty());

        // when
        AuthException exception = assertThrows(AuthException.class,
                () -> authInternalService.login(request));

        // then
        assertEquals(AuthErrorCode.USER_WITHDRAWN, exception.getErrorCode());
    }

    @Test
    void 로그인_비밀번호가_틀린_경우_AuthException을_반환한다() {
        // given
        AuthLoginRequest request = new AuthLoginRequest("name", "WrongPassword!");
        User user = User.create("name", "encodedPassword", "email@example.coim", "홍길동", Role.USER);
        given(authRepository.findByUsernameAndDeletedAtIsNull(request.username())).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(false);

        // when
        AuthException exception = assertThrows(AuthException.class,
                () -> authInternalService.login(request));

        // then
        assertEquals(AuthErrorCode.INVALID_LOGIN, exception.getErrorCode());
    }

    @Test
    void 회원탈퇴_사용자가_없는_경우_AuthException을_반환한다() {
        // given
        AuthWithdrawRequest request = new AuthWithdrawRequest("Password!");
        given(authRepository.findById(1L)).willReturn(Optional.empty());

        // when
        AuthException exception = assertThrows(AuthException.class,
                () -> authInternalService.withdraw(request, 1L));

        // then
        assertEquals(CommonErrorCode.INVALID_USER, exception.getErrorCode());
    }

    @Test
    void 회원탈퇴_비밀번호가_틀린_경우_AuthException을_반환한다() {
        // given
        AuthWithdrawRequest request = new AuthWithdrawRequest("WrongPassword!");
        User user = User.create("name", "encodedPassword", "email@example.coim", "홍길동", Role.USER);
        given(authRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(false);

        // when
        AuthException exception = assertThrows(AuthException.class,
                () -> authInternalService.withdraw(request, 1L));

        // then
        assertEquals(AuthErrorCode.PASSWORD_MISSMATCH, exception.getErrorCode());
    }

    @Test
    void 회원탈퇴에_성공하면_deletedAt이_설정된다() {
        // given
        AuthWithdrawRequest request = new AuthWithdrawRequest("Password!");
        User user = User.create("name", "encodedPassword", "email@example.coim", "홍길동", Role.USER);

        given(authRepository.findById(1L)).willReturn(Optional.of(user));
        given(passwordEncoder.matches(request.password(), user.getPassword())).willReturn(true);

        // when
        authInternalService.withdraw(request, 1L);

        // then
        assertNotNull(user.getDeletedAt());
        assertTrue(user.getDeletedAt().isBefore(LocalDateTime.now().plusSeconds(1)));

        verify(passwordEncoder, times(1)).matches(request.password(), user.getPassword());
    }
}
