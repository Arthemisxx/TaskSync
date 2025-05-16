package com.example.demo.services;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import com.example.demo.mappers.SubtaskMapper;
import com.example.demo.models.DTOs.SubtaskDTO;
import com.example.demo.repositories.SubtaskRepository;
import com.example.demo.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    public HttpStatus removeSubtaskById(Long subtaskId) {
        Optional<SubtaskEntity> target = subtaskRepository.findById(subtaskId); //.ifPresent(subtaskRepository::delete);
        if (target.isPresent()) {
            subtaskRepository.delete(target.get());
            return HttpStatus.OK;
        }
        return HttpStatus.NOT_FOUND;
    }
}
