package com.example.demo.services;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.TeamMapper;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Set;

@RequiredArgsConstructor
@Service
public class TeamService {
    private final TeamMapper teamMapper;
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

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
}
