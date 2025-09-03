package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {

    default Task findByIdOrElseThrow(long id) {
        return findById(id).orElseThrow(() -> new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
