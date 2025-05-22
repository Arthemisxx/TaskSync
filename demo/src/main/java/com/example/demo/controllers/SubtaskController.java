package com.example.demo.controllers;

import com.example.demo.mappers.SubtaskMapper;
import com.example.demo.models.DTOs.SubtaskDTO;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.services.SubtaskService;
import com.example.demo.services.TeamService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/subtasks")
public class SubtaskController {
    private final SubtaskService subtaskService;
    private final SubtaskMapper subtaskMapper;
    private final Logger logger = LogManager.getLogger(TeamController.class);


    @GetMapping(path = "/get-all/{mainTaskId}")
    public ResponseEntity<List<SubtaskDTO>> getAllSubtasks(@PathVariable Long mainTaskId){
        List<SubtaskDTO> result = subtaskService.getAllSubtaskByMainTask(mainTaskId);
        if(result.isEmpty()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
    }

    @DeleteMapping(path = "/remove/{id}")
    public ResponseEntity<Void>  removeSubtask(@PathVariable Long id){
        HttpStatus result = subtaskService.removeSubtaskById(id);
        return new ResponseEntity<>(result);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<SubtaskDTO> updateSubtask(@RequestBody SubtaskDTO subtaskDTO){
        logger.info("Updating subtask...");

        SubtaskDTO updatedSubtask = subtaskService.updateSubtask(subtaskMapper.toEntity(subtaskDTO));

        if(updatedSubtask == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedSubtask, HttpStatus.OK);
    }
}
