package com.example.taskflow.domain.auth.util;

import com.example.taskflow.domain.user.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

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
}
