package com.example.taskflow.domain.auth.util;

import com.example.taskflow.domain.auth.exception.AuthErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.user.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Slf4j
@Component
public class JwtProvider {

    private final Key key;
    private final long EXPIRE_TIME;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    public JwtProvider(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.token.expire-time}") long expireTime
    ) {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.EXPIRE_TIME = expireTime;
    }

    /**
     * JWT 토큰 생성
     *
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param role 사용자 역할(Role Enum)
     * @return JWT 토큰 문자열
     */
    public String createToken(Long userId, String email, Role role) {
        Date date = new Date();

        return Jwts.builder()
                        .setSubject(String.valueOf(userId))
                        .claim("email", email)
                        .claim("role", role.name())
                        .setExpiration(new Date(date.getTime() + EXPIRE_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    /**
     * 토큰으로부터 받은 정보를 기반으로 Authentication 객체 반환
     *
     * @param token
     * @return Authentication
     */
    public Authentication getAuthentication(String token) {
        return new UsernamePasswordAuthenticationToken(
                getUserId(token),
                null,
                createAuthorityList(getRole(token))
        );
    }

    /**
     * 요청 헤더의 'Authorization' 필드에서 토큰 추출
     *
     * @param request
     * @return 토큰 문자열
     */
    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");

        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }

        throw new AuthException(AuthErrorCode.TOKEN_MISSING);
    }

    /**
     * 토큰 정보 검증
     *
     * @param token
     * @return
     */
    public void validateToken(String token) {
        try {
             Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        } catch (UnsupportedJwtException | IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * JWT 토큰에서 Claims 추출
     *
     * @param token JWT 문자열
     * @return Claims 객첸
     */
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Token에서 User ID 추출
     *
     * @param token
     * @return User ID
     */
    public Long getUserId(String token) {
        return Long.parseLong(
                extractClaims(token)
                        .getSubject()
        );
    }

    /**
     * Token에서 Email 추출
     *
     * @param token
     * @return Email
     */
    public String getEmail(String token) {
        return extractClaims(token)
                .get("email", String.class);
    }

    /**
     * Token에서 Role 추출
     *
     * @param token
     * @return Role
     */
    public String getRole(String token) {
        return extractClaims(token)
                .get("role", String.class);
    }
}
