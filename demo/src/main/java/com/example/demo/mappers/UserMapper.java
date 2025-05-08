package com.example.demo.mappers;

import com.example.demo.entities.UserEntity;
import com.example.demo.models.DTOs.UserDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserEntity toEntity(UserDTO user) {
        //TODO
        return new UserEntity();
    }

    public UserDTO toDTO(UserEntity user){
        //TODO
        return new UserDTO();
    }
}
