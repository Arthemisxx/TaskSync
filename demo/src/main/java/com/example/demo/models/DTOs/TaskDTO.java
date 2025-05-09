package com.example.demo.models.DTOs;

import com.example.demo.models.TaskPriority;
import com.example.demo.models.TaskStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class TaskDTO {
    private Long id;
    private String title;
    private String description;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime fromDate;
    @JsonFormat(pattern="yyyy-MM-dd'T'HH:mm:ss", shape = JsonFormat.Shape.STRING)
    private LocalDateTime toDate;
    private TaskStatus status;
    private TaskPriority priority;
    private Set<SubtaskDTO> subtasks;
    private Long assignedUserId;
    private Long teamId;
}
