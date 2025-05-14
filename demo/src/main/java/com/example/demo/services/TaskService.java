package com.example.demo.services;

import com.example.demo.entities.TaskEntity;
import com.example.demo.mappers.TaskMapper;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.repositories.SubtaskRepository;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final TaskMapper taskMapper;
    private final SubtaskService subtaskService;
    private final UserRepository userRepository;

    public TaskEntity findById(Long id) {
        return taskRepository.findTaskEntityById(id);
    }

    public void addTask(TaskDTO task) {
        TaskEntity taskEntity = taskMapper.toEntity(task);
        taskRepository.save(taskEntity);
        System.out.println(taskEntity.getId());
        taskEntity.getSubtasks().forEach(s -> subtaskService.addSubtask(s, taskEntity));
        
    }

    public List<TaskDTO> findByUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(auth.getName());
        List<TaskEntity> userTasks = new ArrayList<>(taskRepository.findAllByCreator(user.get()));

        return userTasks.stream()
                .map(taskMapper::toDTO)
                .toList();


    }

    public List<TaskDTO> findByTeamId(Long teamId) {
        return null;
    };
}
