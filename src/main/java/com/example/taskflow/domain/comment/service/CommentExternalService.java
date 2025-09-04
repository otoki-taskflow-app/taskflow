package com.example.taskflow.domain.comment.service;

import com.example.taskflow.domain.comment.entity.Comment;
import com.example.taskflow.domain.comment.exception.CommentErrorCode;
import com.example.taskflow.domain.comment.exception.InvalidCommentException;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CommentExternalService {
    private final CommentRepository commentRepository;

    public Comment getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new InvalidCommentException(CommentErrorCode.COMMENT_NOT_FOUND));
    }
}
