package com.example.demo.mappers;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.TeamDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class TeamMapper {

    public TeamEntity toEntity(TeamDTO dto, Set<UserEntity> users) {
        TeamEntity entity = new TeamEntity();
        entity.setId(dto.getId());
        entity.setTeamName(dto.getTeamName());
        entity.setDescription(dto.getDescription());
        entity.setUsers(users != null ? users : new HashSet<>());
        return entity;
    }

    public TeamDTO toDTO(TeamEntity entity) {
        TeamDTO dto = new TeamDTO();
        dto.setId(entity.getId());
        dto.setTeamName(entity.getTeamName());
        dto.setDescription(entity.getDescription());
        if (entity.getUsers() != null) {
            dto.setUserIds(entity.getUsers().stream()
                    .map(UserEntity::getId)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }
}
