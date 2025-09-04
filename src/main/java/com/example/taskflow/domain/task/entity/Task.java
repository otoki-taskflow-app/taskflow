package com.example.taskflow.domain.task.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Task extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private LocalDateTime dueDate;

    @Enumerated(EnumType.STRING)
    private Priority priority;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User assignee;

    public Task(String title, String description, Priority priority, LocalDateTime dueDate, Status status, User assignee) {
        this.title = title;
        this.description = description;
        this.priority = priority;
        this.dueDate = dueDate;
        this.status = status;
        this.assignee = assignee;
    }

    public void updateTask(String title, String description, LocalDateTime dueDate, Priority priority, Status status, User assignee) {
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.priority = priority;
        this.status = status;
        this.assignee = assignee;
    }

    public void statusUpdate(Status status) {
        this.status = status;
    }

    public void deleteTask() {
        deletedAt = LocalDateTime.now();
    }
}
