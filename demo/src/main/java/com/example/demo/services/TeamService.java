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

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
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
//        var user = userRepository.findByEmail(auth.getName()).get();
        Optional<UserEntity> userOpt = userRepository.findByEmail(auth.getName());
        if (userOpt.isEmpty()) {
            throw new RuntimeException("User not found with email: " + auth.getName());
        }
        UserEntity user = userOpt.get();
//        team.getUserIds().add(user.getId());
        if (team.getUserIds() == null) {
            team.setUserIds(new HashSet<>());
        }
        team.getUserIds().add(user.getId());
        TeamEntity teamEntity = teamMapper.toEntity(team);
        teamRepository.save(teamEntity);
        Set<UserEntity> userEntities = teamEntity.getUsers();
        userEntities.forEach(u -> {
            u.getTeams().add(teamEntity);
            userRepository.save(u);
        });
    }

    public List<TeamDTO> findTeamsByCurrentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        var user = userRepository.findByEmail(auth.getName()).get();
        List<TeamEntity> teams = teamRepository.findAllByUsersIs(Set.of(user));
        if( teams.isEmpty()) return null;
        return teams.stream().map(teamMapper::toDTO).toList();
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

    public UserEntity deleteUserFromTeam(Long teamId, Long userId) {
        UserEntity user = userService.findUserById(userId);
        TeamEntity team = this.findTeamById(teamId);

        if (user == null || team == null) {
            return null;
        }

        if(!team.getUsers().contains(user)){
            return null;
        }
        team.getUsers().remove(user);
        teamRepository.save(team);
        user.getTeams().remove(team);
        userRepository.save(user);
        logger.info(user.getFirstName()+ " " + user.getLastName() + " removed successfully to team: " + team.getTeamName());
        return user;
    }


    public TeamDTO updateTeam(TeamEntity entity) {

        TeamEntity team = this.findTeamById(entity.getId());

        if(team == null) {
            return null;
        }

        team.setTeamName(entity.getTeamName());
        team.setDescription(entity.getDescription());

        teamRepository.save(team);

        return teamMapper.toDTO(team);
    }
}
