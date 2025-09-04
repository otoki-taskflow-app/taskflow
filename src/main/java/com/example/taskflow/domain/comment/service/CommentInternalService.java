package com.example.taskflow.domain.comment.service;

import com.example.taskflow.domain.comment.dto.request.CommentCreateRequest;
import com.example.taskflow.domain.comment.dto.response.CommentCreateResponse;
import com.example.taskflow.domain.comment.dto.response.CommentUserResponse;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.exception.CommentErrorCode;
import com.example.taskflow.domain.comment.exception.InvalidCommentException;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInternalService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentCreateResponse createComment(CommentCreateRequest request, Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.TASK_NOT_FOUND));

        Comment comment = new Comment(request.getContent(), user, task);
        Comment savedComment = commentRepository.save(comment);
        return CommentCreateResponse.from(savedComment, CommentUserResponse.from(savedComment));
    }
}
