package com.example.taskflow.domain.auth.service;

import com.example.taskflow.domain.auth.dto.response.TokenResponse;
import com.example.taskflow.domain.auth.entity.RefreshToken;
import com.example.taskflow.domain.auth.exception.AuthErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.RefreshTokenRepository;
import com.example.taskflow.domain.auth.security.JwtProvider;
import com.example.taskflow.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TokenInternalService {

    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * 로그인 성공 시 Access Token과 Refresh Token 생성 또는 갱신
     *
     * @param user 로그인한 User 엔티티
     * @return 발급된 Access Token
     */
    @Transactional
    public String generateOrUpdateTokens(User user) {
        TokenResponse token = jwtProvider.createToken(user.getId(), user.getEmail(), user.getRole());

        RefreshToken existingToken = refreshTokenRepository.findByUserId(user.getId()).orElse(null);

        if (existingToken != null) {
            existingToken.updateToken(token.refreshToken(), token.refreshTime());
        } else {
            refreshTokenRepository.save(
                    RefreshToken.create(user, token.refreshToken(), token.refreshTime())
            );
        }

        return token.accessToken();
    }

    @Transactional
    public void logout(Long userId) {
        refreshTokenRepository.findByUserId(userId)
                .ifPresent(refreshTokenRepository::delete);
    }

    @Transactional(readOnly = true)
    public RefreshToken findValidRefreshToken(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .filter(token -> !token.isExpired())
                .orElseThrow(() -> new AuthException(AuthErrorCode.TOKEN_EXPIRED));
    }

    @Transactional(readOnly = true)
    public boolean hasRefreshToken(Long userId) {
        return refreshTokenRepository.findByUserId(userId)
                .map(token -> !token.isExpired())
                .orElse(false);
    }
}
