package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequest;
import com.example.taskflow.domain.comment.dto.response.CommentCreateResponse;
import com.example.taskflow.domain.comment.service.CommentInternalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/tasks/{taskId}/comments")
public class CommentController {
    private final CommentInternalService commentInternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
            @RequestBody CommentCreateRequest commentCreateRequest,
            @RequestParam Long userId, // 이 부분은 나중에
            @PathVariable Long taskId) {
        CommentCreateResponse response = commentInternalService.createComment(commentCreateRequest, userId, taskId);
        return ApiResponse.created(response, "Comment가 생성되었습니다. ");
    }
}
