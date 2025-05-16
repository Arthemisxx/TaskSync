package com.example.demo.services;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import com.example.demo.mappers.SubtaskMapper;
import com.example.demo.models.DTOs.SubtaskDTO;
import com.example.demo.repositories.SubtaskRepository;
import com.example.demo.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;
    private final TaskRepository taskRepository;
    private final SubtaskMapper subtaskMapper;

    public void addSubtask(SubtaskEntity subtask, TaskEntity mainTask) {
        subtask.setMainTask(mainTask);
        subtaskRepository.save(subtask);
    }

    public List<SubtaskDTO> getAllSubtaskByMainTask(Long mainTaskId) {
        TaskEntity mainTask = taskRepository.findTaskEntityById(mainTaskId);
        return subtaskRepository.findAllByMainTask(mainTask).stream().map(subtaskMapper::toDTO).toList();
    }
}
