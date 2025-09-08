package com.example.taskflow.domain.comment.entity;

import com.example.taskflow.common.entity.BaseEntity;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id") // 부모 댓글 (null이면 일반 댓글)
    private Comment parentId;

    private String content;

    private Comment(String content, User user, Task task, Comment parentId) { // 생성자
        this.content = content;
        this.user = user;
        this.task = task;
        this.parentId = parentId;
    }

    public static Comment create(String content, User user, Task task, Comment parentId) {
        return new Comment(content, user, task, parentId);
    }

    public void updateComment(String content){
        this.content = content;
    }

    public boolean isReply() { // 대댓글인지 판단
        return this.parentId != null;
    }
}
