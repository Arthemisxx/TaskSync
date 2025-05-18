package com.example.demo.controllers;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.TeamMapper;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.models.DTOs.UserDTO;
import com.example.demo.services.TeamService;
import com.example.demo.services.UserService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@RestController
@RequestMapping("/teams")
public class TeamController {
    private final Logger logger = LogManager.getLogger(TeamController.class);
    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final UserService userService;


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


    @PostMapping(path = "/add-user/{teamId}/{userId}")
    public ResponseEntity<Void> addUser(@PathVariable Long teamId, @PathVariable Long userId) {
        logger.info("Adding user {} to team {}", userId, teamId);

        Optional<UserEntity> user = userService.findUserById(userId);
        TeamEntity team = teamService.findTeamById(teamId);

        if (user.isEmpty() || team == null) {
            return ResponseEntity.notFound().build();
        }

        TeamDTO teamDTO = teamMapper.toDTO(team);
        teamDTO.getUserIds().add(user.get().getId());
        teamDTO.getUserIds().forEach(u -> {
            logger.info("TEEST: Adding user {} to team ", u);
        });


        team = teamMapper.toEntity(teamDTO);
        team.getUsers().forEach(u -> {
            logger.info("TEEST!!: Adding user {} to team ", u.getId());
        });
        teamService.save(team);

        logger.info("User {} added to team {}", userId, teamId);
        return ResponseEntity.ok().build();
    }



}
