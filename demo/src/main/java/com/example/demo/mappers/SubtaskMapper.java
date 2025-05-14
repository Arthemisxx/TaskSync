package com.example.demo.mappers;

import com.example.demo.entities.SubtaskEntity;
import com.example.demo.models.DTOs.SubtaskDTO;
import org.springframework.stereotype.Component;

@Component
public class SubtaskMapper {
    public SubtaskEntity toEntity(SubtaskDTO subtask){
        Long id = subtask.getId();
        String content = subtask.getContent();
        Boolean status = subtask.getStatus();
        return new SubtaskEntity(id, content, status);
    }

    public SubtaskDTO toDTO(SubtaskEntity subtask){
        Long id = subtask.getId();
        String content = subtask.getContent();
        Boolean status = subtask.getStatus();
        return new SubtaskDTO(id, content, status);
    }
}
