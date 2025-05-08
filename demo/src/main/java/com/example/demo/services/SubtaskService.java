package com.example.demo.services;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.entities.TaskEntity;
import com.example.demo.repositories.SubtaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SubtaskService {
    private final SubtaskRepository subtaskRepository;

    public void addSubtask(SubtaskEntity subtask, TaskEntity mainTask) {
        subtask.setMainTask(mainTask);
        subtaskRepository.save(subtask);
    }
}
