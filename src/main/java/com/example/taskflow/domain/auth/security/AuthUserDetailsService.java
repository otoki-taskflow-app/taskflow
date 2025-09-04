package com.example.taskflow.domain.auth.security;

import com.example.taskflow.common.exception.CommonErrorCode;
import com.example.taskflow.domain.auth.exception.AuthException;
import com.example.taskflow.domain.auth.repository.AuthRepository;
import com.example.taskflow.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service("userDetailsService")
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthUserDetailsService implements UserDetailsService {

    private final AuthRepository authRepository;

    /**
     * username으로 사용자 조회 후 SecurityUserDetails 객체 반환
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = authRepository.findByUsername(username).orElseThrow(()
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
