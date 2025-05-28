package com.example.demo.services;

import com.example.demo.entities.UserEntity;
import com.example.demo.mappers.UserMapper;
import com.example.demo.models.DTOs.UserDTO;
import com.example.demo.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserEntity> allUsers() {
        return userRepository.findAll();
    }

    public UserEntity findUserById(Long id) {
        return userRepository.getUserEntityById(id) ;
    }

    public UserDTO findUserByIdtoDTO(Long id) {
        return userMapper.toDTO(userRepository.getUserEntityById(id)) ;
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserDTO updateUser(UserEntity entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserEntity currentUser = (UserEntity) authentication.getPrincipal();

        currentUser.setFirstName(entity.getFirstName());
        currentUser.setLastName(entity.getLastName());
        currentUser.setAvatar(entity.getAvatar());
        currentUser.setEmail(entity.getEmail());
        userRepository.save(currentUser);

        return userMapper.toDTO(currentUser);
    }
}
