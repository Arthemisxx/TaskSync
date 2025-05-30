package com.example.demo.mappers;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.SubtaskDTO;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {
    private final UserRepository userRepository;
    private final SubtaskMapper subtaskMapper;
    private final TeamRepository teamRepository;

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
//        var a = (UserEntity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        UserEntity a = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        UserEntity creator = userRepository.getUserEntityById(a.getId());
        TeamEntity team = teamRepository.findTeamEntityById(task.getTeamId());
        return new TaskEntity(id, title,description, fromDate, toDate, status, taskPriority, subtasks, creator, assignedUser, team);
    }

    public TaskDTO toDTO(TaskEntity task){
        Long id = task.getId();
        String title = task.getTitle();
        String description = task.getDescription();
        LocalDateTime fromDate = LocalDateTime.now();
        LocalDateTime toDate = task.getToDate();
        TaskStatus status = task.getStatus();
        TaskPriority taskPriority = task.getPriority();
        Set<SubtaskDTO> subtasks= task.getSubtasks()
                .stream()
                .map(subtaskMapper::toDTO)
                .collect(Collectors.toSet());
        Long assignedUserId = task.getAssignedUser().getId();
        Long teamId = task.getTeam().getId();
        return new TaskDTO(id, title, description, fromDate, toDate, status, taskPriority, subtasks, assignedUserId, teamId);
    }
}
