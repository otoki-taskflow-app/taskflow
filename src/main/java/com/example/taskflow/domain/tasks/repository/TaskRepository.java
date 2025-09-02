package com.example.taskflow.domain.tasks.repository;

import com.example.taskflow.domain.tasks.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
