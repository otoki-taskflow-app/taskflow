package com.example.taskflow.domain.auth.service;

import com.example.taskflow.domain.auth.repository.AuthRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class AuthInternalService {

    private final AuthRepository authRepository;
}
