package com.example.demo.controllers;

import com.example.demo.entities.TeamEntity;
import com.example.demo.mappers.TeamMapper;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.services.TeamService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final Logger logger = LogManager.getLogger(TeamController.class);
    private final TeamService teamService;
    private final TeamMapper teamMapper;


    @PostMapping(path = "/create-team")
    public ResponseEntity<Void> createTeam(@RequestBody TeamDTO team){
        logger.info("Adding team...");
        teamService.createTeam(team);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = "/team-id/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id){
        logger.info("Getting team by id...");
        TeamEntity res = teamService.findTeamById(id);

//        ResponseEntity<TeamDTO> res = new ResponseEntity<>(teamMapper.toDTO(teamService.findTeamById(id)), HttpStatus.OK);
        if(res == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(teamMapper.toDTO(teamService.findTeamById(id)), HttpStatus.OK);
    }
}
