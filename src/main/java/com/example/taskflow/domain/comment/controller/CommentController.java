package com.example.taskflow.domain.comment.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.common.response.PageResponse;
import com.example.taskflow.domain.auth.security.annotation.CurrentUser;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequest;
import com.example.taskflow.domain.comment.dto.request.CommentUpdateRequest;
import com.example.taskflow.domain.comment.dto.response.CommentCreateResponse;
import com.example.taskflow.domain.comment.dto.response.CommentGetResponse;
import com.example.taskflow.domain.comment.dto.response.CommentUpdateResponse;
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

    @PostMapping // CREATE
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createComment(
            @RequestBody CommentCreateRequest request,
            @CurrentUser Long userId,
            @PathVariable Long taskId) {
        CommentCreateResponse response = commentInternalService.createComment(request, userId, taskId);
        return ApiResponse.created(response, "댓글이 생성되었습니다. ");
    }

    @PatchMapping("/{commentId}") // UPDATE
    public ResponseEntity<ApiResponse<CommentUpdateResponse>> updateComment(
            @RequestBody CommentUpdateRequest request,
            @PathVariable Long taskId,
            @PathVariable Long commentId) {

        CommentUpdateResponse response = commentInternalService.updateComment(request, taskId, commentId);
        return ApiResponse.success(response, "댓글이 수정되었습니다.");
    }

    @GetMapping // READ
    public ResponseEntity<ApiResponse<PageResponse<CommentGetResponse>>> getComments(
            @PathVariable Long taskId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "newest") String sort
    ) {
        PageResponse<CommentGetResponse> response = commentInternalService.getComments(taskId, page, size, sort);
        return ApiResponse.success(response, "댓글 목록 조회에 성공했습니다.");
    }

    @DeleteMapping("/{commentId}") // DELETE
    public ResponseEntity<ApiResponse<Void>> deleteComment(
            @CurrentUser Long userId,
            @PathVariable Long taskId,
            @PathVariable Long commentId
    ) {
        String message = commentInternalService.deleteComment(userId, taskId, commentId);
        return ApiResponse.deleteSuccess(message);
    }

    @PostMapping("/{parentId}/replies")
    public ResponseEntity<ApiResponse<CommentCreateResponse>> createReplyComment(
            @RequestBody CommentCreateRequest request,
            @CurrentUser Long userId,
            @PathVariable Long taskId,
            @PathVariable Long parentId
    ) {
        CommentCreateResponse response = commentInternalService.createReplyComment(request, userId, taskId, parentId);
        return ApiResponse.created(response, "대댓글이 생성되었습니다.");
    }
}
