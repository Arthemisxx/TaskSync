package com.example.demo.mappers;

import com.example.demo.entities.TeamEntity;
import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.TeamDTO;
import com.example.demo.models.DTOs.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamEntity toEntity(TeamDTO team) {
        //TODO
        return new TeamEntity();
    }

    public TeamDTO toDTO(TeamEntity team) {
        //TODO
        return new TeamDTO();
    }
}
