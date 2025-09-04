package com.example.taskflow.domain.comment.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Column(nullable = false, length = 100)
    private String content;


    public Comment(String content, User user, Task task) { // 생성자
        this.content = content;
        this.user = user;
        this.task = task;
    }

    public void updateComment(String content){
        this.content = content;
    }

}
