package com.example.demo.controllers;

import com.example.demo.models.DTOs.SubtaskDTO;
import com.example.demo.services.SubtaskService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
