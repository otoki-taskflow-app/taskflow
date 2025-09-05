package com.example.taskflow.domain.comment.service;

import com.example.taskflow.common.response.PageResponse;
import com.example.taskflow.domain.comment.dto.request.CommentCreateRequest;
import com.example.taskflow.domain.comment.dto.request.CommentUpdateRequest;
import com.example.taskflow.domain.comment.dto.response.*;
import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.exception.CommentErrorCode;
import com.example.taskflow.domain.comment.exception.InvalidCommentException;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentInternalService {
    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Transactional // CREATE
    public CommentCreateResponse createComment(CommentCreateRequest request, Long userId, Long taskId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.TASK_NOT_FOUND));

        Comment comment = new Comment(request.content(), user, task, null);
        Comment savedComment = commentRepository.save(comment);
        return CommentCreateResponse.from(savedComment, CommentUserResponse.from(savedComment));
    }

    @Transactional // UPDATE
    public CommentUpdateResponse updateComment(CommentUpdateRequest request, Long taskId,  Long commentId) {
        taskRepository.findById(taskId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.TASK_NOT_FOUND));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (!comment.getTask().getId().equals(taskId)) {
            throw new InvalidCommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
        }

        comment.updateComment(request.content());
        Comment updatedComment = commentRepository.save(comment);
        return CommentUpdateResponse.from(updatedComment, CommentUserResponse.from(updatedComment));
    }

    @Transactional(readOnly = true) //READ
    public PageResponse<CommentGetResponse> getComments(Long taskId, int page, int size, String sort) {
        taskRepository.findById(taskId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.TASK_NOT_FOUND));

        Sort order = "oldest".equalsIgnoreCase(sort)
                ? Sort.by("createdAt").ascending()
                : Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page, size, order);
        Page<CommentGetResponse> pageResult = commentRepository.findByTask_Id(taskId, pageable)
                .map(comment -> CommentGetResponse.from(comment, CommentUserResponse.from(comment)));

        return PageResponse.fromPage(pageResult);
    }

    @Transactional // DELETE
    public void deleteComment(Long userId, Long taskId, Long commentId) {
        userRepository.findById(userId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.USER_NOT_FOUND));
        taskRepository.findById(taskId).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.TASK_NOT_FOUND));

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));
        if (!comment.getUser().getId().equals(userId)){
            throw new InvalidCommentException(CommentErrorCode.COMMENT_USER_MISMATCH);
        }
        if (!comment.getTask().getId().equals(taskId)) {
            throw new InvalidCommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
        }
        commentRepository.delete(comment);
    }

    @Transactional // 대댓글
    public CommentCreateResponse createReplyComment(CommentCreateRequest request, Long userId, Long taskId, Long parentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.USER_NOT_FOUND));
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.TASK_NOT_FOUND));
        Comment parent = commentRepository.findById(parentId)
                .orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));

        if (parent.isReply()) {
            throw new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND);
        }
        if (!parent.getTask().getId().equals(taskId)) {
            throw new InvalidCommentException(CommentErrorCode.COMMENT_TASK_MISMATCH);
        }

        Comment reply = new Comment(request.content(), user, task, parent);
        Comment saved = commentRepository.save(reply);
        return CommentCreateResponse.from(saved, CommentUserResponse.from(saved));
    }
}
