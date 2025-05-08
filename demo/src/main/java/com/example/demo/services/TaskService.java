package com.example.demo.services;

import com.example.demo.entities.TaskEntity;
import com.example.demo.mappers.TaskMapper;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.repositories.SubtaskRepository;
import com.example.demo.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final SubtaskRepository subtaskRepository;
    private final TaskMapper taskMapper;
    private final SubtaskService subtaskService;

    public TaskEntity findById(Long id) {
        return taskRepository.findTaskEntityById(id);
    }

    public void addTask(TaskDTO task) {
        TaskEntity taskEntity = taskMapper.toEntity(task);
        taskRepository.save(taskEntity);
        System.out.println(taskEntity.getId());
        taskEntity.getSubtasks().forEach(s -> subtaskService.addSubtask(s, taskEntity));
        
    }
}
