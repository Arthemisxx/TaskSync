package com.example.demo.controllers;

import com.example.demo.entities.TaskEntity;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final Logger logger = LogManager.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TaskEntity> getTaskById(@PathVariable Long id){
        return new ResponseEntity<>(taskService.findById(id), HttpStatus.OK);
    }

    @PostMapping(path = "/add")
    public ResponseEntity<Void> addTask(@RequestBody TaskDTO task){
        logger.info("Adding task...");
        taskService.addTask(task);
        return new ResponseEntity<>(HttpStatus.OK);
    }






}
