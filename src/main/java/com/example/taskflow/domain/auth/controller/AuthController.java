package com.example.taskflow.domain.auth.controller;

import com.example.taskflow.domain.auth.service.AuthInternalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthInternalService authInternalService;


}
