package com.example.demo.services;

import com.example.demo.entities.TaskEntity;
import com.example.demo.entities.TeamEntity;
import com.example.demo.mappers.TaskMapper;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.repositories.SubtaskRepository;
import com.example.demo.repositories.TaskRepository;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
    private final TeamRepository teamRepository;
    private final Logger logger = LogManager.getLogger(TaskService.class);

    public TaskEntity findById(Long id) {
        return taskRepository.findTaskEntityById(id);
    }

    public void addTask(TaskDTO task) {
        TaskEntity taskEntity = taskMapper.toEntity(task);
        taskRepository.save(taskEntity);
        taskEntity.getSubtasks().forEach(s -> subtaskService.addSubtask(s, taskEntity));
    }

    public List<TaskDTO> findAssignedTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(auth.getName());
        List<TaskEntity> userTasks = new ArrayList<>(taskRepository.findAllByAssignedUser(user.get()));

        return userTasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public List<TaskDTO> findCreatedTasks() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(auth.getName());
        List<TaskEntity> userTasks = new ArrayList<>(taskRepository.findAllByCreator(user.get()));

        return userTasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    }

    public List<TaskDTO> findByTeamId(Long teamId) {
        var team = teamRepository.findTeamEntityById(teamId);
        List<TaskEntity> userTasks = new ArrayList<>(taskRepository.findAllByTeam(team));

        return userTasks.stream()
                .map(taskMapper::toDTO)
                .toList();
    };

    @Transactional
    public void removeTask(Long id) {
        var task = taskRepository.findTaskEntityById(id);
        subtaskRepository.deleteAllByMainTask(task);
        taskRepository.deleteById(id);
        logger.info("Task deleted successfully!");
    }


    public TaskDTO updateTask(TaskEntity entity) {

        TaskEntity task = taskRepository.findTaskEntityById(entity.getId());

        if(task == null) {
            return null;
        }

        task.setTitle(entity.getTitle());
        task.setDescription(entity.getDescription());
        task.setFromDate(entity.getFromDate());
        task.setToDate(entity.getToDate());
        task.setStatus(entity.getStatus());
        task.setPriority(entity.getPriority());
        task.setCreator(entity.getCreator());
        task.setAssignedUser(entity.getAssignedUser());
        task.setTeam(entity.getTeam());

        taskRepository.save(task);

        return taskMapper.toDTO(task);
    }
}
