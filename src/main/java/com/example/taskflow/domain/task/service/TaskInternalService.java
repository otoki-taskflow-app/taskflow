package com.example.taskflow.domain.task.service;

import com.example.taskflow.domain.task.dto.Request.TaskCreateRequest;
import com.example.taskflow.domain.task.dto.Request.TaskStatusUpdateRequest;
import com.example.taskflow.domain.task.dto.Request.TaskUpdateRequest;
import com.example.taskflow.domain.task.dto.Response.*;
import com.example.taskflow.domain.task.entity.Task;
import com.example.taskflow.domain.task.enums.Status;
import com.example.taskflow.domain.task.exception.InvalidTaskException;
import com.example.taskflow.domain.task.exception.TaskErrorCode;
import com.example.taskflow.domain.task.repository.TaskRepository;
import com.example.taskflow.domain.user.entity.User;
import com.example.taskflow.domain.user.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class TaskInternalService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * 새로운 Task 를 생성하는 메서드
     * 유저가 입력한 정보를 Task Entity로 만들어 저장
     *
     * @param request 유저가 입력한 내용
     * @return 생성된 태스크의 응답 DTO로 변환 후 반환
     * @throws InvalidTaskException 담당자 Id에 해당하는 유저를 찾을 수 없을 경우 발생
     */
    @Transactional
    public TaskCreateResponse createTask(TaskCreateRequest request) {

        // TODO : UserExternalService 가 생기면 추후 리팩토링
        User user = userRepository.findById(request.assigneeId()).orElseThrow(()->new InvalidTaskException(TaskErrorCode.USER_NOT_FOUND));

        Task task = new Task(
                request.title(),
                request.description(),
                request.priority(),
                request.dueDate(),
                Status.TODO,
                user
        );

        Task savedTask = taskRepository.save(task);

        return TaskCreateResponse.from(savedTask);
    }

    /**
     * 삭제되지 않은 Task 전체를 조회하는 메서드
     * Soft delete 된 task는 제외 (deletedAt IS NULL 조건만 조회)
     *
     * @param pageable 페이지네이션 정보
     * @return 조회된 태스크 목록을 TaskGetResponse DTO의 Page로 반환
     */
    @Transactional (readOnly=true)
    public Page<TaskGetResponse> getAllTasks(Pageable pageable) {

        Page<Task> tasks = taskRepository.findAllByDeletedAtIsNull(pageable);

        return tasks.map(TaskGetResponse::from);
    }

    /**
     * 삭제되지 않은 Task 를 조회하는 메서드
     * Soft delete 된 task는 오류 발생
     *
     * @param taskId 해당 task의 ID
     * @return 조회된 Task 정보를 DTO로 반환
     * @throws InvalidTaskException 해당 ID의 Task가 존재하지 않는 경우 발생
     */
    @Transactional (readOnly = true)
    public TaskGetResponse getTask(Long taskId) {

        // task 조회 없다면 InvalidTaskException
        Task findTask = taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId);

        return TaskGetResponse.from(findTask);
    }

    /**
     * Task 를 수정하는 메서드
     * Soft delete 된 task는 오류 발생
     *
     * @param taskId 해당 task의 ID
     * @param request 유저가 입력한 내용
     * @return 수정된 Task 정보를 DTO로 반환
     * @throws InvalidTaskException 해당 ID의 Task 와 User가 존재하지 않는 경우 발생
     */
    @Transactional
    public TaskUpdateResponse updateTask(Long taskId, @Valid TaskUpdateRequest request) {

        // user 와 task 조회 없다면 InvalidTaskException
        User findUser = userRepository.findById(request.assigneeId()).orElseThrow(()->new InvalidTaskException(TaskErrorCode.USER_NOT_FOUND));
        Task findTask = taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId);

        findTask.updateTask(
                request.title(),
                request.description(),
                request.dueDate(),
                request.priority(),
                request.status(),
                findUser
        );

        return TaskUpdateResponse.from(findTask);
    }

    /**
     * Task 의 Status 를 수정하는 메서드
     * Soft delete 된 task는 오류 발생
     *
     * @param taskId 해당 Task 의 ID
     * @param request 유저가 입력한 status
     * @return 수정된 Task 정보를 DTO로 반환
     * @throws InvalidTaskException 해당 ID의 Task가 존재하지 않는 경우 발생
     */
    public TaskStatusUpdateResponse statusUpdate(Long taskId, @Valid TaskStatusUpdateRequest request) {

        // task 조회 없다면 InvalidTaskException
        Task findTask = taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId);

        findTask.statusUpdate(request.status());

        return TaskStatusUpdateResponse.from(findTask);
    }

    /**
     * Task 를 삭제하는 메서드
     * Soft delete 로 처리
     *
     * @param taskId 해당 Task 의 ID
     * @throws InvalidTaskException 해당 ID의 Task가 존재하지 않는 경우 발생
     */
    public void delete(Long taskId) {

        // task 조회 없다면 InvalidTaskException
        Task findTask = taskRepository.findByIdAndDeletedAtIsNullOrElseThrow(taskId);

        // task soft delete
        findTask.deleteTask();
    }
}
