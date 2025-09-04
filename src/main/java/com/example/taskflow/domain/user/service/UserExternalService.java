package com.example.taskflow.domain.user.service;


import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserExternalService {

    private final UserRepository userRepository;

    // 0. 회원 가입 시 유저 정보 저장
//    @Transactional
//    public User save(User user) {
//        return userRepository.save(user);
//    }

    // 1. 유저 단일 조회 (탈퇴한 유저 제외)
    @Transactional(readOnly = true)
    public Optional<User> findByIdAndDeletedAtIsNull(Long id) {
        return userRepository.findByIdAndDeletedAtIsNull(id);
    }

    // 2. 팀원 목록 조회 (탈퇴한 유저 제외)
    @Transactional(readOnly = true)
    public List<User> findAllByIdAndDeletedAtIsNull(List<Long> userIds) {
        return userRepository.findAllById(userIds).stream()
                .filter(user -> user.getDeletedAt() == null)
                .toList();
    }

}
