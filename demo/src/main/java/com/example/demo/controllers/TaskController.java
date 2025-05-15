package com.example.demo.controllers;

import com.example.demo.entities.TaskEntity;
import com.example.demo.models.DTOs.TaskDTO;
import com.example.demo.services.TaskService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final Logger logger = LogManager.getLogger(TaskController.class);

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @GetMapping("/search-assigned")
    public ResponseEntity <List<TaskDTO>> getUserTasks(){
        List<TaskDTO> result = taskService.findAssignedTasks();
        if(result.isEmpty()){
            logger.info("No tasks found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            result.forEach(e-> logger.info(e.getTitle()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @GetMapping("/search-crated")
    public ResponseEntity <List<TaskDTO>> getCreatedTasks(){
        List<TaskDTO> result = taskService.findCreatedTasks();
        if(result.isEmpty()){
            logger.info("No tasks found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            result.forEach(e-> logger.info(e.getTitle()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @GetMapping("/search-by-team/{teamId}")
    public ResponseEntity<List<TaskDTO>> getTeamTasks(@PathVariable Long teamId){
        List<TaskDTO> result = taskService.findByTeamId(teamId);
        if(result.isEmpty()){
            logger.info("No tasks found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            result.forEach(e-> logger.info(e.getTitle()));
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
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

    @DeleteMapping("/remove/{id}")
    public ResponseEntity<Void> removeTask(@PathVariable Long id){
        logger.info("Deleting task...");
        TaskEntity taskToRemove = taskService.findById(id);
        if(taskToRemove == null){
            logger.info("Element not found!");
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            taskService.removeTask(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }



}
