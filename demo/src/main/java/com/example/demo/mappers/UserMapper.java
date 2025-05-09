package com.example.demo.mappers;

import com.example.demo.entities.UserEntity;
import com.example.demo.entities.TeamEntity;
import com.example.demo.models.DTOs.UserDTO;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserEntity toEntity(UserDTO dto, Set<TeamEntity> teams) {
        UserEntity entity = new UserEntity();
        entity.setId(dto.getId());
        entity.setFirst_name(dto.getFirstName());
        entity.setLast_name(dto.getLastName());
        entity.setAvatar(dto.getAvatar());
        entity.setEmail(dto.getEmail());
        entity.setRole(dto.getRole());
        entity.setTeams(teams != null ? teams : new HashSet<>());
        return entity;
    }

    public UserDTO toDTO(UserEntity entity) {
        UserDTO dto = new UserDTO();
        dto.setId(entity.getId());
        dto.setFirstName(entity.getFirst_name());
        dto.setLastName(entity.getLast_name());
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
