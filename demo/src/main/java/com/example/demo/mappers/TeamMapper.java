package com.example.demo.mappers;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class TeamMapper {

    private final UserRepository userRepository;


    public TeamEntity toEntity(TeamDTO dto) {
        TeamEntity entity = new TeamEntity();
        entity.setId(dto.getId());
        entity.setTeamName(dto.getTeamName());
        entity.setDescription(dto.getDescription());
        Set<UserEntity> users = dto.getUserIds().stream().map(userRepository::getUserEntityById).collect(Collectors.toSet());
        entity.setUsers(users);
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
