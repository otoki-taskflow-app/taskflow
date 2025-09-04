package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskExternalService {

    private final TaskRepository taskRepository;

    public Task getTaskById(Long taskId) {
        return taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId);
    }
}
