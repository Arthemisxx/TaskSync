package com.example.demo.mappers;

import com.example.demo.entities.UserEntity;
import com.example.demo.entities.TeamEntity;
import com.example.demo.models.DTOs.UserDTO;
import com.example.demo.repositories.TeamRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    private final TeamRepository teamRepository;

    public UserMapper(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public UserEntity toEntity(UserDTO dto) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setAvatar(dto.getAvatar());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        Set<TeamEntity> teams = new HashSet<>();
        if(dto.getTeamIds()!= null){
             teams = dto.getTeamIds().stream().map(teamRepository::getTeamEntityById).collect(Collectors.toSet());
        }
        entity.setTeams(teams);
        return entity;
    }

    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setAvatar(entity.getAvatar());
        dto.setEmail(entity.getEmail());
        dto.setRole(entity.getRole());
        if (entity.getTeams() != null) {
            dto.setTeamIds(entity.getTeams().stream()
                    .map(TeamEntity::getId)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }
}
