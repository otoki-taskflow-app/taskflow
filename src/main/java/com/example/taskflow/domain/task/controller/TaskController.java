package com.example.taskflow.domain.task.controller;

import com.example.taskflow.common.response.ApiResponse;
import com.example.taskflow.domain.task.dto.Request.TaskCreateRequest;
import com.example.taskflow.domain.task.dto.Response.TaskCreateResponse;
import com.example.taskflow.domain.task.service.TaskInternalService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("/api/tasks")
public class TaskController {

    private final TaskInternalService taskInternalService;

    @PostMapping
    public ResponseEntity<ApiResponse<TaskCreateResponse>> createTask(
            @RequestBody TaskCreateRequest taskCreateRequest,
            @RequestBody Long userId
    ) {

        TaskCreateResponse response = taskInternalService.createTask(taskCreateRequest, userId);

        return ApiResponse.created(response, "Task가 생성되었습니다.");
    }
}
