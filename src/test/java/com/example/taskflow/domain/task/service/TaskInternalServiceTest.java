package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.Request.TaskCreateRequest;
import com.example.taskflow.domain.task.dto.Request.TaskStatusUpdateRequest;
import com.example.taskflow.domain.task.dto.Request.TaskUpdateRequest;
import com.example.taskflow.domain.task.dto.Response.TaskCreateResponse;
import com.example.taskflow.domain.task.dto.Response.TaskGetResponse;
import com.example.taskflow.domain.task.dto.Response.TaskStatusUpdateResponse;
import com.example.taskflow.domain.task.dto.Response.TaskUpdateResponse;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Priority;
import com.example.taskflow.domain.task.enums.Status;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.enums.Role;
import com.example.taskflow.domain.user.service.UserExternalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class TaskInternalServiceTest {

    @Mock
    private TaskRepository taskRepository;
    @Mock
    private UserExternalService userExternalService;
    @InjectMocks
    private TaskInternalService taskInternalService;

    private static final String TASK_TITLE = "테스트 제목";
    private static final String TASK_DESCRIPTION = "테스트 내용";
    private static final Priority TASK_PRIORITY = Priority.MEDIUM;
    private static final Status TASK_STATUS = Status.TODO;
    private static final User TEST_USER = new User("아이디", "비밀번호", "이메일", "이름", Role.USER);

    @Test
    public void Task_등록할때_assigneeId가_null_이면_로그인유저로_할당한다() {
        // given
        Long loginUserId = 1L;

        TaskCreateRequest request = new TaskCreateRequest(
                TASK_TITLE,
                TASK_DESCRIPTION,
                LocalDateTime.now().plusDays(1),
                TASK_PRIORITY,
                null
        );

        User loginUser = TEST_USER;
        ReflectionTestUtils.setField(loginUser, "id", loginUserId);

        given(userExternalService.getUserById(loginUserId)).willReturn(loginUser);
        given(taskRepository.save(any(Task.class))).willAnswer(inv -> inv.getArgument(0));

        // when
        TaskCreateResponse response = taskInternalService.createTask(request, loginUserId);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task savedTask = captor.getValue();

        assertEquals(TASK_TITLE, savedTask.getTitle());
        assertEquals(TASK_DESCRIPTION, savedTask.getDescription());
        assertEquals(TASK_PRIORITY, savedTask.getPriority());
        assertEquals(Status.TODO, savedTask.getStatus());
        assertEquals(loginUserId, savedTask.getAssignee().getId());

        assertNotNull(response);
    }

    @Test
    public void Task가_정상_등록된다() {
        // given
        Long loginUserId = 1L;
        Long assigneeId = 2L;

        TaskCreateRequest request = new TaskCreateRequest(
                TASK_TITLE,
                TASK_DESCRIPTION,
                LocalDateTime.now().plusDays(1),
                TASK_PRIORITY,
                assigneeId
        );

        User assignee = TEST_USER;
        ReflectionTestUtils.setField(assignee, "id", assigneeId);

        Task task = Task.create(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), Status.TODO, assignee);

        given(userExternalService.getUserById(assigneeId)).willReturn(assignee);
        given(taskRepository.save(any(Task.class))).willReturn(task);

        // when
        TaskCreateResponse response = taskInternalService.createTask(request, loginUserId);

        // then
        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task savedTask = captor.getValue();

        assertEquals(TASK_TITLE, savedTask.getTitle());
        assertEquals(TASK_DESCRIPTION, savedTask.getDescription());
        assertEquals(TASK_PRIORITY, savedTask.getPriority());
        assertEquals(Status.TODO, savedTask.getStatus());
        assertEquals(assigneeId, savedTask.getAssignee().getId());

        assertNotNull(response);
    }

    @Test
    public void getAllTasks가_정상_조회된다() {
        // given
        Long task1Id = 1L;
        Long task2Id = 2L;

        Task task1 = new Task(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), TASK_STATUS, TEST_USER);
        Task task2 = new Task(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), Status.IN_PROGRESS, TEST_USER);

        ReflectionTestUtils.setField(task1, "id", task1Id);
        ReflectionTestUtils.setField(task2, "id", task2Id);

        Page<Task> tasks = new PageImpl<>(List.of(task1, task2));

        given(taskRepository.findAllByDeletedAtIsNull(any(Pageable.class))).willReturn(tasks);

        // when
        Page<TaskGetResponse> result = taskInternalService.getAllTasks(PageRequest.of(0, 10), null);

        // then
        assertEquals(2, result.getContent().size());
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(0, result.getNumber());

        assertEquals(task1.getId(), result.getContent().get(0).id());
        assertEquals(task2.getId(), result.getContent().get(1).id());
    }

    @Test
    public void getTask가_정상_조회된다() {
        // given
        Long taskId = 1L;
        Long assigneeId = 2L;
        User user = TEST_USER;

        ReflectionTestUtils.setField(user, "id", assigneeId);

        Task task = new Task(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), TASK_STATUS, user);

        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId)).willReturn(task);

        // when
        TaskGetResponse result = taskInternalService.getTask(taskId);

        // then
        assertEquals(taskId, result.id());
        assertEquals(TASK_TITLE, result.title());
        assertEquals(TASK_DESCRIPTION, result.description());
        assertEquals(TASK_PRIORITY, result.priority());
        assertEquals(TASK_STATUS, result.status());
        assertEquals(assigneeId, result.assigneeId());
    }

    @Test
    public void getTask_중_Task가_존재하지_않아_에러가_발생한다() {
        // given
        Long taskId = 1L;

        when(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId))
                .thenThrow(new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));

        // when & then
        InvalidTaskException exception = assertThrows(InvalidTaskException.class, () -> {
            taskInternalService.getTask(taskId);
        });

        assertEquals(TaskErrorCode.TASK_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void updateTask가_정상적으로_업데이트된다() {
        // given
        Long taskId = 1L;
        Long assigneeId1 = 2L;
        Long assigneeId2 = 3L;

        User user1 = new User(TEST_USER.getUsername(), TEST_USER.getPassword(), TEST_USER.getEmail(), TEST_USER.getName(), TEST_USER.getRole());
        User user2 = new User(TEST_USER.getUsername(), TEST_USER.getPassword(), TEST_USER.getEmail(), TEST_USER.getName(), TEST_USER.getRole());

        ReflectionTestUtils.setField(user1, "id", assigneeId1);
        ReflectionTestUtils.setField(user2, "id", assigneeId2);

        Task task = new Task(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), TASK_STATUS, user1);

        ReflectionTestUtils.setField(task, "id", taskId);

        TaskUpdateRequest request = new TaskUpdateRequest(
                "수정된 타이틀",
                "수정된 내용",
                LocalDateTime.now().plusDays(2),
                Priority.HIGH,
                Status.IN_PROGRESS,
                assigneeId2
        );

        given(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId)).willReturn(task);
        given(userExternalService.getUserById(assigneeId2)).willReturn(user2);

        // when
        TaskUpdateResponse response = taskInternalService.updateTask(taskId, request);

        // then

        assertEquals(request.title(), response.title());
        assertEquals(request.description(), response.description());
        assertEquals(request.dueDate(), response.dueDate());
        assertEquals(request.priority(), response.priority());
        assertEquals(request.status(), response.status());
        assertEquals(request.assigneeId(), response.assigneeId());
    }

    @Test
    public void updateTask_중_Task가_존재하지_않아_에러가_발생한다() {
        // given
        Long taskId = 1L;
        Long assigneeId = 2L;

        TaskUpdateRequest request = new TaskUpdateRequest(
                "수정된 타이틀",
                "수정된 내용",
                LocalDateTime.now().plusDays(2),
                Priority.HIGH,
                Status.IN_PROGRESS,
                assigneeId
        );

        when(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId))
                .thenThrow(new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));

        // when & then
        InvalidTaskException exception = assertThrows(InvalidTaskException.class, () -> {
            taskInternalService.updateTask(taskId, request);
        });

        assertEquals(TaskErrorCode.TASK_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void statusUpdate가_정상적으로_업데이트된다() {
        // given
        Long taskId = 1L;
        Long assigneeId = 2L;

        User user = TEST_USER;

        ReflectionTestUtils.setField(user, "id", assigneeId);

        Task task = new Task(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), TASK_STATUS, user);

        ReflectionTestUtils.setField(task, "id", taskId);

        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(Status.IN_PROGRESS);

        given(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId)).willReturn(task);

        // when
        TaskStatusUpdateResponse response = taskInternalService.statusUpdate(taskId, request);

        // then
        assertEquals(request.status(), response.status());
    }

    @Test
    public void statusUpdate_중_Task가_존재하지_않아_에러가_발생한다() {
        // given
        Long taskId = 1L;
        TaskStatusUpdateRequest request = new TaskStatusUpdateRequest(Status.IN_PROGRESS);

        when(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId))
                .thenThrow(new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));

        // when & then
        InvalidTaskException exception = assertThrows(InvalidTaskException.class, () -> {
            taskInternalService.statusUpdate(taskId, request);
        });

        assertEquals(TaskErrorCode.TASK_NOT_FOUND.getMessage(), exception.getMessage());
    }

    @Test
    public void Task가_정상적으로_삭제된다() {
        // given
        Long taskId = 1L;
        Long assigneeId = 2L;

        User user = TEST_USER;

        ReflectionTestUtils.setField(user, "id", assigneeId);

        Task task = new Task(TASK_TITLE, TASK_DESCRIPTION, TASK_PRIORITY, LocalDateTime.now().plusDays(1), TASK_STATUS, user);

        ReflectionTestUtils.setField(task, "id", taskId);

        given(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId)).willReturn(task);

        // when
        taskInternalService.delete(taskId);

        // then
        assertNotNull(task.getDeletedAt());
    }

    @Test
    public void Task가_삭제되던_중_Task가_존재하지_않아_에러가_발생한다() {
        // given
        Long taskId = 1L;

        when(taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId))
                .thenThrow(new InvalidTaskException(TaskErrorCode.TASK_NOT_FOUND));

        // when & then
        InvalidTaskException exception = assertThrows(InvalidTaskException.class, () -> {
            taskInternalService.delete(taskId);
        });

        assertEquals(TaskErrorCode.TASK_NOT_FOUND.getMessage(), exception.getMessage());
    }
}
