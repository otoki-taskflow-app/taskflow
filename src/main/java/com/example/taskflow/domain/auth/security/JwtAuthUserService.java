package com.example.taskflow.domain.auth.security;

import com.example.taskflow.common.exception.CommonErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.AuthRepository;
import com.example.taskflow.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class JwtAuthUserService {

    private final AuthRepository authRepository;

    public AuthUser loadAuthUser(Long userId) {
        User user = authRepository.findById(userId).orElseThrow(()
                -> new AuthException(CommonErrorCode.INVALID_USER));

        AuthUserDto authUserDto = AuthUserDto.from(user);

        return new AuthUser(
                authUserDto,
                Collections.singleton(
                        new SimpleGrantedAuthority(authUserDto.getRole().toString())
                )
        );
    }
}
