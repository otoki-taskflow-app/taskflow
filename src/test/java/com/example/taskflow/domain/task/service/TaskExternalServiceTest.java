package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TaskExternalServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @InjectMocks
    private TaskExternalService taskExternalService;

    @Test
    public void Task가_정상적으로_반환된다() {
        // given
        Long taskId = 1L;
        Long assigneeId = 2L;
        User assignee = new User("아이디", "비밀번호", "이메일", "이름", Role.USER);

        ReflectionTestUtils.setField(assignee, "id", assigneeId);

        Task task = new Task("테스트 제목", "테스트 내용", Priority.MEDIUM, LocalDateTime.now().plusDays(1), Status.TODO, assignee);

        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId)).willReturn(task);

        // when
        Task result = taskExternalService.getTaskById(taskId);

        // then
        verify(taskRepository).findByIdAndDeletedAtIsNullOrElseThrow(taskId);
        assertEquals(taskId, result.getId());
        assertEquals(task, result);
    }

    @Test
    public void getTaskById_중_Task가_존재하지_않아_에러가_발생한다() {
        // given
        Long taskId = 1L;

        when(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId))
                .thenThrow(new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));

        // when & then
        InvalidTaskException exception = assertThrows(InvalidTaskException.class, () -> {
            taskExternalService.getTaskById(taskId);
        });

        assertEquals(TaskErrorCode.TASK_NOT_FOUND.getMessage(), exception.getMessage());
    }
}
