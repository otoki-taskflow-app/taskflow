package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.search.dto.TaskSummary;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskExternalService {

    private final TaskRepository taskRepository;

    public Task getTaskById(Long taskId) {
        return taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId);
    }

    public List<TaskSummary> searchByKeyword(String keyword) {
        return taskRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword)
                .stream()
                .map(TaskSummary::from)
                .toList();
    }
}
