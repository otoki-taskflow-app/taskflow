package com.example.taskflow.domain.auth.security;

import lombok.AllArgsConstructor;
import lombok.Getter;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 *  Spring Security에서 사용하는 UserDetails 구현체
 *
 *  AuthUserDto를 기반으로 Security 객체 생성
 *  JWT 인증/권한 검증 시 사용
 */
@Getter
@AllArgsConstructor
public class AuthUser implements UserDetails {

    private AuthUserDto authUserDto;
    private Collection<GrantedAuthority> authorities;

    @Override
    public String getPassword() {
        return authUserDto.getPassword();
    }

    @Override
    public String getUsername() {
        return authUserDto.getUsername();
    }

    /**
     * 사용자의 권한 목록 반환
     *
     * @return
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }
}
