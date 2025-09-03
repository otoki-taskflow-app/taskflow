package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamExternalService {
    private final TeamRepository teamRepository;


}
