package com.example.taskflow.domain.task.repository;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @EntityGraph(attributePaths = "user")
    Page<Task> findAllByDeletedAtIsNull(Pageable pageable);

    Optional<Task> findByIdAndDeletedAtIsNull(Long id);

    default Task findByIdAndDeletedAtIsNullOrElseThrow(Long id) {
        return findByIdAndDeletedAtIsNull(id).orElseThrow(() -> new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));
    }
}
