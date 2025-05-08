package com.example.demo.mappers;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final UserMapper userMapper;
    private final TeamMapper teamMapper;
    private final UserRepository userRepository;
    private final SubtaskMapper subtaskMapper;

    public TaskEntity toEntity(TaskDTO task){
        Long id = task.getId();
        String title = task.getTitle();
        String description = task.getDescription();
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = task.getToDate();
        TaskStatus status = task.getStatus();
        TaskPriority taskPriority = task.getPriority();
        Set<SubtaskEntity> subtasks = task.getSubtasks()
                .stream()
                .map(subtaskMapper::toEntity)
                .collect(Collectors.toSet());
        UserEntity assignedUser = userRepository.getUserEntityById(task.getAssignedUserId());
        // TODO add creator from securityContext
        // TODO add team from team repo
        return new TaskEntity(id, title,description, fromDate, toDate, status, taskPriority, subtasks, null, null, null);
    }

    public TaskDTO toDTO(TaskEntity task){
        //TODO
        return new TaskDTO();
    }
}
