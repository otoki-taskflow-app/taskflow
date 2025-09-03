package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.Request.TaskCreateRequest;
import com.example.taskflow.domain.task.dto.Response.TaskCreateResponse;
import com.example.taskflow.domain.task.dto.Response.TaskUserResponse;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Status;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskInternalService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public TaskCreateResponse createTask(TaskCreateRequest request, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(()->new InvalidTaskException(TaskErrorCode.USER_NOT_FOUND));

        Task task = new Task(
                request.title(),
                request.description(),
                request.priority(),
                request.dueDate(),
                Status.TODO,
                user
        );

        Task savedTask = taskRepository.save(task);

        return TaskCreateResponse.from(savedTask, TaskUserResponse.from(savedTask));
    }
}
