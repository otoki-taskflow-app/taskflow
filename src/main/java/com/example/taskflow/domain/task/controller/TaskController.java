package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.common.response.PageResponse;
import com.example.taskflow.domain.task.dto.Request.TaskCreateRequest;
import com.example.taskflow.domain.task.dto.Request.TaskStatusUpdateRequest;
import com.example.taskflow.domain.task.dto.Request.TaskUpdateRequest;
import com.example.taskflow.domain.task.dto.Response.TaskCreateResponse;
import com.example.taskflow.domain.task.dto.Response.TaskGetResponse;
import com.example.taskflow.domain.task.dto.Response.TaskStatusUpdateResponse;
import com.example.taskflow.domain.task.dto.Response.TaskUpdateResponse;
import com.example.taskflow.domain.task.service.TaskInternalService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskInternalService taskInternalService;

    // task 생성
    @PostMapping
    public ResponseEntity<ApiResponse<TaskCreateResponse>> createTask(
            @RequestBody @Valid TaskCreateRequest taskCreateRequest
    ) {

        TaskCreateResponse response = taskInternalService.createTask(taskCreateRequest);

        return ApiResponse.created(response, "Task가 생성되었습니다.");
    }

    // task 목록 조회
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<TaskGetResponse>>> getAllTasks(
            @PageableDefault Pageable pageable
    ) {

        Page<TaskGetResponse> response = taskInternalService.getAllTasks(pageable);

        return ApiResponse.pageSuccess(response, "Task 목록을 조회했습니다.");
    }

    // task 상세 조회
    @GetMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskGetResponse>> getTask(@PathVariable Long taskId) {

        TaskGetResponse response = taskInternalService.getTask(taskId);

        return ApiResponse.success(response, "Task를 조회했습니다.");
    }

    // task 수정
    @PutMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskUpdateResponse>> updateTask(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskUpdateRequest request
    ) {

        TaskUpdateResponse response = taskInternalService.updateTask(taskId, request);

        return ApiResponse.success(response, "Task가 수정되었습니다.");
    }

    // task 상태 업데이트
    @PatchMapping("/{taskId}")
    public ResponseEntity<ApiResponse<TaskStatusUpdateResponse>> statusUpdate(
            @PathVariable Long taskId,
            @RequestBody @Valid TaskStatusUpdateRequest request
    ) {

        TaskStatusUpdateResponse response = taskInternalService.statusUpdate(taskId, request);

        return ApiResponse.success(response, "작업 상태가 업데이트되었습니다.");
    }

    // task 삭제
    @DeleteMapping("/{taskId}")
    public ResponseEntity<ApiResponse<Void>> deleteTask(@PathVariable Long taskId) {

        taskInternalService.delete(taskId);

        return ApiResponse.deleteSuccess("Task가 삭제되었습니다.");
    }
}
