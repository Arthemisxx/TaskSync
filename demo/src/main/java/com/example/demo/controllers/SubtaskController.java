package com.example.demo.controllers;

import com.example.demo.models.DTOs.SubtaskDTO;
import com.example.demo.services.SubtaskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
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

    @GetMapping(path = "/{mainTaskId}")
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
}
