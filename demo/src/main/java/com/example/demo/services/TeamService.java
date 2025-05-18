package com.example.demo.services;

import com.example.demo.controllers.TeamController;
import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.TeamMapper;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final Logger logger = LogManager.getLogger(TeamService.class);
    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final UserService userService;

    public void createTeam(TeamDTO team) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(auth.getName()).get();
        team.getUserIds().add(user.getId());
        TeamEntity teamEntity = teamMapper.toEntity(team);
        teamRepository.save(teamEntity);
        Set<UserEntity> userEntities = teamEntity.getUsers();
        userEntities.forEach(u -> {
            u.getTeams().add(teamEntity);
            userRepository.save(u);
        });
    }

    public TeamEntity findTeamById(Long teamId) {
        return teamRepository.findTeamEntityById(teamId);
    }

    public UserEntity addUserToTeam(Long teamId, Long userId) {
        UserEntity user = userService.findUserById(userId);
        TeamEntity team = this.findTeamById(teamId);

        if (user == null || team == null) {
            return null;
        }

        if(team.getUsers().contains(user)){
            return null;
        }

        team.getUsers().add(user);
        teamRepository.save(team);
        user.getTeams().add(team);
        userRepository.save(user);
        logger.info(user.getFirstName()+ " " + user.getLastName() + " added successfully to team: " + team.getTeamName());
        return user;
    }

//    public void save(TeamEntity team) {
//        teamRepository.save(team);
//    }
}
