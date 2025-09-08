package com.example.taskflow.domain.comment.repository;

import com.example.taskflow.domain.comment.entity.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @EntityGraph(attributePaths = {"user", "task"}) // n+1
    Page<Comment> findByTask_Id(Long taskId, Pageable pageable);

    List<Comment> findByParentId_Id(Long parentId); // 댓글 삭제시 해당 댓글의 대댓글 삭제를 위한 쿼리
}