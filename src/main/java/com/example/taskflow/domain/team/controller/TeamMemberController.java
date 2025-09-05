package com.example.taskflow.domain.team.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.team.dto.Request.TeamMemberRequest;
import com.example.taskflow.domain.team.dto.Response.TeamMemberResponse;
import com.example.taskflow.domain.team.dto.Response.TeamResponse;
import com.example.taskflow.domain.team.service.TeamMemberExternalService;
import com.example.taskflow.domain.team.service.TeamMemberInternalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teams")
public class TeamMemberController {
    private final TeamMemberExternalService teamMemberExternalService;
    private final TeamMemberInternalService teamMemberInternalService;

    //팀 멤버 목록 조회
    @GetMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<List<TeamMemberResponse>>> getTeamMember(@PathVariable Long teamId) {
        List<TeamMemberResponse> team = teamMemberExternalService.getMemberByTeamId(teamId);
        return ApiResponse.success(team,"팀 멤버 목록을 조회했습니다.");
    }

    //팀 멤버 추가
    @PostMapping("/{teamId}/members")
    public ResponseEntity<ApiResponse<TeamResponse>> addMember(@PathVariable Long teamId,@Valid @RequestBody TeamMemberRequest teamMemberRequest){
        TeamResponse team = teamMemberInternalService.createTeamMember(teamId, teamMemberRequest.getUserId());
        return ApiResponse.success(team,"멤버가 성공적으로 추가되었습니다.");
    }

    //팀 멤버 제거
    @DeleteMapping("/{teamId}/members/{memberId}")
    public ResponseEntity<ApiResponse<TeamResponse>> deleteMember(@PathVariable Long teamId,@Valid @PathVariable Long memberId){
        TeamResponse team = teamMemberInternalService.deleteTeamMember(teamId, memberId);
        return ApiResponse.success(team,"멤버가 성공적으로 제거되었습니다.");
    }

    //추가 가능한 사용자 목록 조회
    /*
    @GetMapping("/{teamId}/members/available")
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAvailableUsers(@PathVariable Long teamId) {
        List<UserResponse> member = teamMemberExternalService.getAvailableUsers(teamId);
        return ApiResponse.success(member, "사용 가능한 사용자 목록을 조회했습니다.");
    }
    */
}
