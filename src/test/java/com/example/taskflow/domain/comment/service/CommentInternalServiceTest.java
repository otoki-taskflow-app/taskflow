package com.example.taskflow.domain.comment.service;

import com.example.taskflow.domain.comment.dto.request.CommentCreateRequest;
import com.example.taskflow.domain.comment.dto.request.CommentUpdateRequest;
import com.example.taskflow.domain.comment.dto.response.CommentCreateResponse;
import com.example.taskflow.domain.comment.dto.response.CommentUpdateResponse;
import com.example.taskflow.domain.comment.entity.Comment;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.BDDMockito.given;
import com.example.taskflow.domain.comment.repository.CommentRepository;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;
import com.example.taskflow.domain.task.service.TaskExternalService;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import com.example.taskflow.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class CommentInternalServiceTest {
    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private TaskExternalService taskExternalService;

    @InjectMocks
    private CommentInternalService commentService;

    @Test // createComment 성공적 생성
    void createComment_success() {
        // given
        Long userId = 1L;
        Long taskId = 100L;
        CommentCreateRequest request = new CommentCreateRequest("테스트 댓글");

        User user = new User(
                "testUser",
                "encodedPassword",
                "test@test.com",
                "홍길동",
                Role.user
        );
        Task task = new Task(
                "테스트 제목",
                "테스트 설명",
                Priority.HIGH,
                LocalDateTime.now().plusDays(1),
                Status.TODO,
                user
        );

        Comment comment = new Comment(request.content(), user, task, null);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(taskExternalService.getTaskById(taskId)).willReturn(task);
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // when
        CommentCreateResponse response = commentService.createComment(request, userId, taskId);

        //then
        assertThat(response).isNotNull();
        assertThat(response.user().id()).isEqualTo(user.getId());
        assertThat(response.taskId()).isEqualTo(task.getId());
    }

    @Test // updateComment 성공적 수정
    void updateComment_success() {
        // given
        Long taskId = 100L;
        Long commentId = 200L;
        CommentUpdateRequest request = new CommentUpdateRequest("수정된 댓글");

        User user = new User(
                "testUser",
                "encodedPassword",
                "test@test.com",
                "홍길동",
                Role.user
        );

        Task task = new Task(
                "테스트 제목",
                "테스트 설명",
                Priority.HIGH,
                LocalDateTime.now().plusDays(1),
                Status.TODO,
                user
        );

        Comment comment = new Comment("원래 댓글", user, task, null);

        given(taskExternalService.getTaskById(taskId)).willReturn(task);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // when
        CommentUpdateResponse response = commentService.updateComment(request, taskId, commentId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.user().id()).isEqualTo(user.getId());
        assertThat(response.taskId()).isEqualTo(task.getId());
    }

    @Test // deleteComment 성공 - 자식 댓글이 있는 경우
    void deleteComment_success_withChildren() {
        // given
        Long userId = 1L;
        Long taskId = 100L;
        Long commentId = 200L;

        User user = new User(
                "testUser",
                "encodedPassword",
                "test@test.com",
                "홍길동",
                Role.user
        );

        Task task = new Task(
                "테스트 제목",
                "테스트 설명",
                Priority.HIGH,
                LocalDateTime.now().plusDays(1),
                Status.TODO,
                user
        );

        // 부모 댓글
        Comment parentComment = new Comment("부모 댓글", user, task, null);

        // 자식 댓글 2개
        Comment child1 = new Comment("자식 댓글1", user, task, parentComment);
        Comment child2 = new Comment("자식 댓글2", user, task, parentComment);
        List<Comment> children = List.of(child1, child2);

        // Mock 설정
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(taskExternalService.getTaskById(taskId)).willReturn(task);
        given(commentRepository.findById(commentId)).willReturn(Optional.of(parentComment));
        given(commentRepository.findByParentId_Id(commentId)).willReturn(children);

        // when
        String resultMessage = commentService.deleteComment(userId, taskId, commentId);

        // then (호출 검증)
        verify(commentRepository).deleteAll(children);    // 자식 댓글 삭제 검증
        verify(commentRepository).delete(parentComment);  // 부모 댓글 삭제 검증
        assertThat(resultMessage).isEqualTo("댓글과 대댓글들이 삭제되었습니다.");
    }
    @Test // createReplyComment 성공적으로 대댓글 생성
    void createReplyComment_success() {
        // given
        Long userId = 1L;
        Long taskId = 100L;
        Long parentId = 200L;
        CommentCreateRequest request = new CommentCreateRequest("대댓글 내용");

        User user = new User(
                "testUser",
                "encodedPassword",
                "test@test.com",
                "홍길동",
                Role.user
        );

        Task task = new Task(
                "테스트 제목",
                "테스트 설명",
                Priority.HIGH,
                LocalDateTime.now().plusDays(1),
                Status.TODO,
                user
        );

        Comment parent = new Comment("부모 댓글", user, task, null);
        Comment reply = new Comment(request.content(), user, task, parent);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(taskExternalService.getTaskById(taskId)).willReturn(task);
        given(commentRepository.findById(parentId)).willReturn(Optional.of(parent));
        given(commentRepository.save(any(Comment.class))).willReturn(reply);

        // when
        CommentCreateResponse response = commentService.createReplyComment(request, userId, taskId, parentId);

        // then
        assertThat(response).isNotNull();
        assertThat(response.content()).isEqualTo("대댓글 내용");
        assertThat(response.parentId()).isEqualTo(parentId);
        assertThat(response.taskId()).isEqualTo(task.getId());
    }

}
