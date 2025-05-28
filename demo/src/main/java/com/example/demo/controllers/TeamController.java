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

import java.util.List;
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

    @GetMapping()
    public ResponseEntity<List<TeamDTO>> getAllUserTeams(){
        var res = teamService.findTeamsByCurrentUser();
        if(res == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }else{
            return new ResponseEntity<>(res, HttpStatus.OK);
        }
    }

    @GetMapping(path = "/team-id/{id}")
    public ResponseEntity<TeamDTO> getTeamById(@PathVariable Long id){
        logger.info("Getting team by id...");
        TeamEntity res = teamService.findTeamById(id);

        if(res == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(teamMapper.toDTO(teamService.findTeamById(id)), HttpStatus.OK);
    }




    @PostMapping(path = "/add-user/{teamId}/{userId}")
    public ResponseEntity<Void> addUser(@PathVariable Long teamId, @PathVariable Long userId) {

        logger.info("Adding user {} to team {}", userId, teamId);

        UserEntity result = teamService.addUserToTeam(teamId,userId);
        if(result == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);

    }

    @DeleteMapping(path = "/remove-user/{teamId}/{userId}")
    public ResponseEntity<Void> removeUser(@PathVariable Long teamId, @PathVariable Long userId) {
        logger.info("Removing user {} from team {}", userId, teamId);

        UserEntity res = teamService.deleteUserFromTeam(teamId, userId);

        if(res == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping(path = "/update")
    public ResponseEntity<TeamDTO> updateTeam(@RequestBody TeamDTO teamDTO){
        logger.info("Updating team...");

        TeamDTO updatedTeam = teamService.updateTeam(teamMapper.toEntity(teamDTO));

        if(updatedTeam == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(updatedTeam, HttpStatus.OK);
    }

    @GetMapping("/{teamId}/users")
    public ResponseEntity<List<UserDTO>> getAllTeamMembers(@PathVariable Long teamId){
        List<UserDTO> users = teamService.findTeamUsers(teamId);

        if(users == null){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

}
