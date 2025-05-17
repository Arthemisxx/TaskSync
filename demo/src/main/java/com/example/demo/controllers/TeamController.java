package com.example.demo.controllers;

import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final Logger logger = LogManager.getLogger(TeamController.class);
    private final TeamService teamService;


    @PostMapping(path = "/create-team")
    public ResponseEntity<Void> createTeam(@RequestBody TeamDTO team){
        logger.info("Adding team...");
        teamService.createTeam(team);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
