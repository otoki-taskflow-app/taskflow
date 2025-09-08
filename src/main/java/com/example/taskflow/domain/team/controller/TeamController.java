package com.example.taskflow.domain.team.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.team.dto.Request.TeamRequest;
import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.service.TeamExternalService;
import com.example.taskflow.domain.team.service.TeamInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teams")
public class TeamController {
    private final TeamExternalService teamExternalService;
    private final TeamInternalService teamInternalService;

    //팀 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<List<TeamResponse>>> getTeam(){
        List<TeamResponse> team = teamExternalService.getTeamList();
        return ApiResponse.success(team, "팀 목록을 조회했습니다.");
    }
    //특정 팀 조회
    @GetMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> getTeamById(@PathVariable Long teamId) {
        TeamResponse team = teamExternalService.getTeamById(teamId);
        return ApiResponse.success(team,"팀 정보를 조회했습니다.");
    }
    //팀 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TeamResponse>> createTeam(@Valid @RequestBody TeamRequest teamRequest){
        TeamResponse team = teamInternalService.createTeam(teamRequest);
        return ApiResponse.created(team,"팀이 성공적으로 생성되었습니다.");
    }
    //팀 정보 수정
    @PutMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> updateTeam(@PathVariable Long teamId,@Valid @RequestBody TeamRequest teamRequest){
        TeamResponse team = teamInternalService.updateTeam(teamId, teamRequest);
        return ApiResponse.success(team,"팀 정보가 성공적으로 업데이트되었습니다.");
    }
    //팀 삭제
    @DeleteMapping("/{teamId}")
    public ResponseEntity<ApiResponse<TeamResponse>> deleteTeam(@PathVariable Long teamId){
        teamInternalService.deleteTeam(teamId);
        return ApiResponse.deleteSuccess("팀이 성공적으로 삭제되었습니다.");
    }
}
