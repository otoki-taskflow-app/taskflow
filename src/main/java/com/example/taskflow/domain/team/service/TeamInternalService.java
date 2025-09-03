package com.example.taskflow.domain.team.service;

import com.example.taskflow.domain.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TeamInternalService {
//팀 생성, 팀 목록 조회, 특정 팀 조회, 팀 멤버 조회, 팀 정보 수정, 팀 삭제, 팀 멤버 추가, 팀 멤버 삭제
    private final TeamRepository teamRepository;

}
