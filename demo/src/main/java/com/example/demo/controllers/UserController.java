package com.example.demo.controllers;

import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.DTOs.UserDTO;
import com.example.demo.services.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final UserMapper userMapper;
    private final Logger logger = LogManager.getLogger(UserController.class);


    @GetMapping("/me")
    public ResponseEntity<UserDTO> authenticatedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();
        logger.info(currentUser.getEmail());

        return ResponseEntity.ok(userMapper.toDTO(currentUser));
    }

    @GetMapping
    public ResponseEntity<List<UserDTO>> allUser(){
        List<UserEntity> users = userService.allUsers();
        users.stream().forEach(u -> logger.info(u.getEmail()));

        return ResponseEntity.ok(users.stream().map(userMapper::toDTO).collect(Collectors.toList()));
    }

    @GetMapping("/search-by-email/{email}")
    public ResponseEntity<UserDTO> searchByEmail(@PathVariable String email){
        Optional<UserEntity> user = userService.findUserByEmail(email);
        if(user.isPresent()){
            return ResponseEntity.ok(userMapper.toDTO(user.get()));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
