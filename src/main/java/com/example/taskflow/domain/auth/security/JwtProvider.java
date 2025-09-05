package com.example.taskflow.domain.auth.security;

import com.example.taskflow.domain.auth.dto.response.TokenResponse;
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
    private final long ACCESS_EXPIRE_TIME;  // Access Token 만료 시간 (밀리초)
    private final long REFRESH_EXPIRE_TIME; // Refresh Token 만료 시간 (밀리초)
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    private final JwtAuthUserService jwtAuthUserService;

    /**
     * JWT Provider 생성자
     *
     * @param secretKey
     * @param accessTime
     * @param refreshTime
     * @param jwtAuthUserService
     */
    public JwtProvider(
            @Value("${jwt.secret.key}") String secretKey,
            @Value("${jwt.token.access-expire-time}") long accessTime,
            @Value("${jwt.token.refresh-expire-time}") long refreshTime,
            JwtAuthUserService jwtAuthUserService
    ) {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        this.key = Keys.hmacShaKeyFor(bytes);
        this.ACCESS_EXPIRE_TIME = accessTime;
        this.REFRESH_EXPIRE_TIME = refreshTime;
        this.jwtAuthUserService = jwtAuthUserService;
    }

    /**
     * 로그인 시 Access Token과 Refresh Token 발급
     *
     * - Access Token: API 호출 시 사용, 클라이언트로 반환
     * - Refresh Token: Redis에 저장, Access Token 재발급 시 사용
     *
     * @param userId 사용자 ID
     * @param email 사용자 이메일
     * @param role 사용자 권한(Role Enum)
     * @return Access Token 문자열
     */
    public TokenResponse createToken(Long userId, String email, Role role) {
        long now = (new Date()).getTime();

        // Access Token 생성
        Date accessTokenExpire = new Date(now + ACCESS_EXPIRE_TIME);
        String accessToken = createAccessToken(userId, email, role, accessTokenExpire);

        // Refresh Token 생성
        Date refreshTokenExpire = new Date(now + REFRESH_EXPIRE_TIME);
        String refreshToken = createRefreshToken(userId, refreshTokenExpire);

        return TokenResponse.of(accessToken, refreshToken, REFRESH_EXPIRE_TIME);
    }

    /**
     * Access Token 생성
     *
     * @param userId
     * @param email
     * @param role
     * @param expireDate
     * @return
     */
    private String createAccessToken(Long userId, String email, Role role, Date expireDate) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .claim("role", role.name())
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    /**
     * Refresh Token 생성
     *
     * @param userId
     * @param expireDate
     * @return
     */
    private String createRefreshToken(Long userId, Date expireDate) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setExpiration(expireDate)
                .signWith(key, signatureAlgorithm)
                .compact();
    }

    public String refreshAccessToken(String token) {
        Claims claims = extractClaimsAllowExpired(token);
        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        Role role = Role.valueOf(claims.get("role", String.class));

        long now = (new Date()).getTime();
        Date newExpire = new Date(now + ACCESS_EXPIRE_TIME);
        return createAccessToken(userId, email, role, newExpire);
    }

    public void validateAccessToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw new AuthException(AuthErrorCode.TOKEN_EXPIRED);
        } catch (SecurityException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException e) {
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    /**
     * 토큰으로부터 받은 정보를 기반으로 Authentication 객체 반환
     *
     * @param token
     * @return Authentication
     */
    public Authentication getAuthentication(String token) {
        Long userId = getUserId(token);

        AuthUser authUser = jwtAuthUserService.loadAuthUser(userId);

        return new UsernamePasswordAuthenticationToken(
                authUser,
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
     * - 유효하지 않은 토큰
     * - 만료된 토큰
     * - 블랙리스트에 있는 토큰
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

    public Claims extractClaimsAllowExpired(String token) {
        try {
            // 정상 토큰이면 그대로
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            // 만료된 토큰이라도 Claims는 반환
            return e.getClaims();
        } catch (JwtException e) {
            // 다른 유효하지 않은 토큰
            throw new AuthException(AuthErrorCode.INVALID_TOKEN);
        }
    }

    public Long getUserIdAllowExpired(String token) {
        return Long.parseLong(extractClaimsAllowExpired(token).getSubject());
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
